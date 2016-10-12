using UnityEngine;
using System.Collections;
using SocketIO;
using System.Collections.Generic;

public class network : MonoBehaviour {

	private SocketIOComponent socket;
	private string message;
	private GameObject player;
	private GameObject camera;

	private float x = 0f;
	private float y = 1.75f;
	private float z = 9f;

	public void Start() 
	{
		Debug.Log("connecting");

		player = GameObject.FindGameObjectWithTag ("Player");
		camera = GameObject.FindGameObjectWithTag ("MainCamera");
		socket = GetComponent<SocketIOComponent>();
		socket.Emit ("init_user");
		socket.On("connection", OnConnected);
		socket.On("new message", OnNewMessage);
		socket.On("move_player", OnPlayerMove);
		socket.On ("update_user", OnNewUser);
		socket.Connect();

	}
	public void OnPlayerMove(SocketIOEvent e) {
	
		Debug.Log (e.data);
	}
	public void OnNewUser(SocketIOEvent e) {
		Dictionary<string,string> dict = new Dictionary<string,string> (){{"player_name", "Bruno"}, {"letters","horse"}};

			var newPlayer = new JSONObject(dict);
			
		newPlayer.str = "{\"player_name\":\"Bruno\", \"letters\":\"Horse\"}";
		socket.Emit ("update_user", newPlayer);
		Debug.Log (newPlayer);
	}

	public void OnConnected(SocketIOEvent e)
	{

		//catch up

		Debug.Log("[SocketIO] Open received: " + e.name + " " + e.data);

		var j = new JSONObject();
		j.str = "VR";
		socket.Emit ("add_name",j);
		var newPlayer = new JSONObject (JSONObject.Type.STRING);
		newPlayer.str = "{player_name:\'Bruno\', letters:\'Horse\'}";
		socket.Emit ("update_user", newPlayer);
		Debug.Log (newPlayer);
		//socket.SendMessage ("{\"player_name\":\"Bruno\", \"letters\":\"Horse\"}");

	}


	public void OnNewMessage(SocketIOEvent e)
	{
		Debug.Log("[SocketIO] New Message received: " + e.name + " " + e.data);
		var username = e.data["author"].str;
		Debug.Log (username);

			message = e.data["msg_content"].str.ToLower();

			Debug.Log ("Message:" + message);

			switch (message) {

			case "reset":
				Dictionary<string,string> dict = new Dictionary<string,string> (){{"player_name", "Bruno"}, {"letters","horse"}};
				var newPlayer = new JSONObject(dict);

				newPlayer.str = "{\"player_name\":\"Bruno\", \"letters\":\"Horse\"}";
				socket.Emit ("update_user", newPlayer);
				Debug.Log (newPlayer);
				player.transform.position = new Vector3 (0f, 1.75f, 9f);
				break;

			case "forward":
				Debug.Log ("Moved forward");
				z += 1f;
				player.transform.position = new Vector3 (x, y, z);
				break;
			case "backward":
				Debug.Log ("Moved backwards");
				z -= 1f;
				player.transform.position = new Vector3 (x, y, z);
				break;
			case "left":
				x -= 1f;
				player.transform.position = new Vector3 (x, y, z);
				break;
			case "score":
				
				var scored = new JSONObject();
				scored.str = "Score!";
				socket.Emit ("new message", scored);
				Debug.Log (scored);
				player.transform.position = new Vector3 (x, y, z);
				break;
			case "right":
				x += 1f;
				player.transform.position = new Vector3 (x, y, z);
				break;
			case "clockwise":
				player.transform.RotateAround (new Vector3 (0f, 1.92f, 0f), Vector3.up, 45f);
				camera.transform.RotateAround (player.transform.position, Vector3.up, 45f);
				break;
			case "counter clockwise":
				player.transform.RotateAround (new Vector3 (0f, 1.92f, 0f), Vector3.up, -45f);
				break;
				

		}
	}
}