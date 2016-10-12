using UnityEngine;
using System.Collections;

public class DisplayText : MonoBehaviour {

	public static string myDirection;

	// Use this for initialization
	void Start () {
	
		myDirection = "TEST";
		GetComponent<TextMesh> ().text = myDirection;
	}
	
	// Update is called once per frame
	void Update (string theString) {
		GetComponent<TextMesh>().text = theString;
	
	}
}
