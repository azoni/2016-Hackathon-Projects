using UnityEngine;
using System.Collections;

public class MouseRotation : MonoBehaviour {

	[SerializeField]
	private Vector2 rotationRange = new Vector3(50, 95);

	[SerializeField]
	private float rotationSensitivity = 0.5f;

	private Vector3 targetAngle;
	private Quaternion startRotation;


	// Use this for initialization
	void Start () {
	
		// Store the starting rotation.
		startRotation = transform.localRotation;

	}


	// Update is called once per frame
	void Update () {
	
		// Add mouse movement to the target angles, multiplied by the rotation sensitivity.
		targetAngle.y += Input.GetAxis("Mouse X") * rotationSensitivity;
		targetAngle.x += Input.GetAxis("Mouse Y") * rotationSensitivity;

		// Lock the X and Y target angles to the rotation range.
		targetAngle.x = Mathf.Clamp(targetAngle.x, -rotationRange.x * 0.5f, rotationRange.x * 0.5f);
		targetAngle.y = Mathf.Clamp(targetAngle.y, -rotationRange.y * 0.5f, rotationRange.y * 0.5f);

		// Update the rotation of the game object by multiplying the start rotation by the target angle rotation.
		transform.localRotation = startRotation * Quaternion.Euler(-targetAngle.x, targetAngle.y, 0);

	}
}
