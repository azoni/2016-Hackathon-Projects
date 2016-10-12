#pragma strict

function OnTriggerEnter () {
	var enemy = transform.GetComponentInParent (Enemy);
	enemy.Die();
}