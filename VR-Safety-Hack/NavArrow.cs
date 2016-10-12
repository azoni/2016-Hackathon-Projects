using UnityEngine;
using System.Collections;

public class ArrowRotate : MonoBehaviour {

  //The goal/objective.
	public GameObject A;

	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
	
		//Rotates object with camera.
		//transform.LookAt(Camera.main.WorldToScreenPoint(transform.position));
		
		//Will rotate arrow towards the object.
		transform.LookAt (A.transform);
	}
}
