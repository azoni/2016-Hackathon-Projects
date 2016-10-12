using UnityEngine;
using System.Collections;
using UnityEngine.UI;

public class GameManager : MonoBehaviour {

	private bool gameStarted = false;
	private float timer;

	[SerializeField]
	private float gameLengthInSeconds = 30f;

	public static int score;

	[SerializeField]
	private Text scoreText;

	[SerializeField]
	private Text timerText;

	[SerializeField]
	private Text gameStateText;

	[SerializeField]
	private AudioSource gameStateSounds;

	[SerializeField]
	private AudioClip gameStartSound;

	[SerializeField]
	private AudioClip gameEndSound;

	// Use this for initialization
	void Start () {
	
		gameStateText.text = "HIT SPACE TO PLAY!";
		timer = gameLengthInSeconds;

		UpdateScoreBoard ();
	}
	
	// Update is called once per frame
	void Update () {
		
		if (!gameStarted && Input.GetKeyUp (KeyCode.Space)) {
		
			StartGame ();
		}

		if (gameStarted){

			timer -= Time.deltaTime;
			UpdateScoreBoard ();
		}

		if (gameStarted && timer <= 0) {

			EndGame ();
		}
		//gameStateText.text = "HIT SPACE TO PLAY!";
	}

	private void StartGame() {

		score = 0;
		gameStarted = true;
		gameStateText.text = "GO! GO! GO!";

		gameStateSounds.PlayOneShot (gameStartSound);
	}

	private void EndGame() {
	
		gameStarted = false;
		timer = gameLengthInSeconds;
		gameStateText.text = "TIME'S UP!\nHIT SPACE TO PLAY AGAIN!";

		gameStateSounds.PlayOneShot (gameEndSound);
	}

	private void UpdateScoreBoard() {

		scoreText.text = "SCORE\n" + score;
		timerText.text = "TIME\n" + Mathf.RoundToInt (timer);

	}
}
