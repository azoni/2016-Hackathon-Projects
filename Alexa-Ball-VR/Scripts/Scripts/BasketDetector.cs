using UnityEngine;
using System.Collections;

public class BasketDetector : MonoBehaviour {

	void OnTriggerExit(Collider other) {
	
		if (other.attachedRigidbody.velocity.y < 0f) {
		
			GameManager.score++;
		}
	}
}
