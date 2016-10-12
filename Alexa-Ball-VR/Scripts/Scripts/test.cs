using UnityEngine;
using System.Collections;

public class test : MonoBehaviour {
	private GameObject Player;
	private float z = 9;
	private float y = 1.75f;
	// Use this for initialization
	void Start () {
		
		Player = GameObject.FindGameObjectWithTag ("Player");
	}

	// Update is called once per frame
	void Update () {
		
		if (Input.GetKeyUp (KeyCode.T)) {

			Player.transform.position = new Vector3 (0f, 1.75f, 9f);
		}
		if (Input.GetKeyUp (KeyCode.Y)) {
			z -= 1;
			Player.transform.position = new Vector3 (0f, y, z);
		}
		if (Input.GetKeyUp (KeyCode.U)) {
			z += 1;
			Player.transform.position = new Vector3 (0f, y, z);
		}
		if (Input.GetKeyUp (KeyCode.I)) {
			y += 1;
			Player.transform.position = new Vector3 (0f, y, z);
		}
		if (Input.GetKeyUp (KeyCode.O)) {
			y -= 1;
			Player.transform.position = new Vector3 (0f, y, z);
		}
	}
}
