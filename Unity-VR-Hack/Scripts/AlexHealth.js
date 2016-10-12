#pragma strict

private var isRestarting = false;

var maxFallDistance = -10;

var GameOverSound : AudioClip;

function Update () {

	if (transform.position.y <= maxFallDistance){

		if (isRestarting == false) {

			RestartLevel();
		}
	}

}

function RestartLevel () {

	isRestarting =  true;
	GetComponent.<AudioSource>().clip = GameOverSound;
	GetComponent.<AudioSource>().Play();
	//gameObject.GameOver.Play();
	yield WaitForSeconds (GetComponent.<AudioSource>().clip.length);
	Application.LoadLevel("Level01");
	//transform.position = CheckPoint.ReachedPoint;
}