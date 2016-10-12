using UnityEngine;
using System.Collections;

public class HideCursor : MonoBehaviour {

	// Use this for initialization
	void Start () {
	
		// Hide the mouse cursor
		Cursor.lockState = CursorLockMode.Locked;
		Cursor.visible = false;

	}

}
