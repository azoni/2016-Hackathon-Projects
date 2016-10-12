#pragma strict

static var ReachedPoint : Vector3;

function OnTriggerEnter(col : Collider) {
	if (col.tag == "Player") {

		ReachedPoint = transform.position;
	}
}