using UnityEngine;
using System.Collections;

public class ThrowObject : MonoBehaviour {

	[SerializeField]
	private GameObject throwingObject;

	private Rigidbody objectRigidBody;

	private Vector3 startingPosition;
	private Quaternion startingRotation;

	private float objectForce = 0f;
	private float maximumForce = 600f;
	private float delay = 1f;
	private float timer;

	[SerializeField]
	private float objectReturnSpeed = 25f;

	[SerializeField]
	private AudioSource objectReturnAudioSource;

	[SerializeField]
	private AudioClip objectReturnAudioClip;
	// Use this for initialization
	void Start () {

		objectRigidBody = throwingObject.GetComponent<Rigidbody>();
		startingPosition = throwingObject.transform.localPosition;
		startingRotation = throwingObject.transform.localRotation;
		objectRigidBody.isKinematic = true;
	}

	// Update is called once per frame
	void Update () {

		if (Input.GetMouseButtonDown(0)) {

			BeginThrow();
		}

		// If the player is continuing to hold down the mouse button
		if (Input.GetMouseButton(0)) {

			PowerUpThrow();
		}

		if (Input.GetMouseButtonUp(0)) {

			ReleaseObject();
		}
	}

	private void BeginThrow() {
	
		objectRigidBody.isKinematic = true;
		objectRigidBody.velocity = Vector3.zero;
		objectRigidBody.angularVelocity = Vector3.zero;

		// return object to start position
		throwingObject.transform.parent = transform;
		throwingObject.transform.localRotation = startingRotation;

		StartCoroutine (ReturnObject());

		objectReturnAudioSource.PlayOneShot (objectReturnAudioClip);

	}

	private void ReleaseObject() {
	
		objectRigidBody.isKinematic = false;
		throwingObject.transform.parent = null;
		objectRigidBody.AddRelativeForce (throwingObject.transform.forward * objectForce);

		objectForce = 0f;
		timer = 0;

		StopCoroutine (ReturnObject ());
	}

	private void PowerUpThrow() {

		// Increment timer once per frame.

		timer += Time.deltaTime;

		if (timer > delay) {
		
			timer = delay;
		}

		float perc = timer / delay;
		objectForce = Mathf.Lerp (0f, maximumForce, perc);

	}

	IEnumerator ReturnObject() {

		float distanceThreshold = 0.1f;
	
		// while there is still a small amount of distance between the thrown object and starting position..
		// move the ball toward the starting position
		// if the thrown object is now close to the start position, reset the position directly.
		// yield back control to the main script

		while (Vector3.Distance (throwingObject.transform.localPosition, startingPosition) 
			> distanceThreshold) {
		
			throwingObject.transform.localPosition = Vector3.Lerp 
				(throwingObject.transform.localPosition, 
				startingPosition, Time.deltaTime * objectReturnSpeed);

			if (Vector3.Distance(throwingObject.transform.localPosition, startingPosition) < distanceThreshold) {

				throwingObject.transform.localPosition = startingPosition;
			}

			yield return null;
		}
	}
}
