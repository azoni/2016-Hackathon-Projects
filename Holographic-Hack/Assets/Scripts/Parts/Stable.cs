using UnityEngine;
using System.Collections;

public class Stable : MonoBehaviour {

    Transform location;
    bool stable;    //stable mode;
	// Use this for initialization
	void Start () {
        stable = false;
    }

    // Update is called once per frame, make sure object cant move;
    void Update()
    {
        if (stable) { 
        this.transform.position = location.position;
        this.transform.rotation = location.rotation;
         }
	}

    public void StableMode()
    {
        location = this.transform;
        stable = true;
    }
}
