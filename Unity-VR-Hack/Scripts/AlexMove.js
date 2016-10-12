#pragma strict

var rotationSpeed = 100;
var jumpHeight = 8;

var Hit01 : AudioClip;
var Hit02 : AudioClip;
var Hit03 : AudioClip;

var distToGround : float;

function Start() {
	// getting the distance from the center to the ground.
	distToGround = GetComponent.<Collider>().bounds.extents.y;
}

function Update() {

	// handle ball rotation
	var rotation : float = Input.GetAxis ("Horizontal") * rotationSpeed;
	rotation *= Time.deltaTime;
	GetComponent.<Rigidbody>().AddRelativeTorque (Vector3.back*rotation);
	// remove isGrounded for unlimited jumps 
	if (Input.GetKeyDown(KeyCode.Space) && IsGrounded()) {

		GetComponent.<Rigidbody>().velocity.y = jumpHeight;	
	}
	if(Input.GetKey(KeyCode.W)){
     transform.Translate(0,0,5*Time.deltaTime);
     }
 	if(Input.GetKey(KeyCode.S)){
     transform.Translate(0,0,-5*Time.deltaTime);
     }


}

function IsGrounded () : boolean {
	return Physics.Raycast(transform.position, -Vector3.up, distToGround + 0.1);
}

function OnCollisionEnter() {
	var theHit = Random.Range(0, 4);
	if (theHit == 0) {
		GetComponent.<AudioSource>().clip = Hit01;
	}
	else if (theHit == 1) {
		GetComponent.<AudioSource>().clip = Hit02;
	}
	else {
		GetComponent.<AudioSource>().clip = Hit03;
	}
	
	GetComponent.<AudioSource>().Play();
}
