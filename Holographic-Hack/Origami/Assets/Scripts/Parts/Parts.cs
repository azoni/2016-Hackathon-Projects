using UnityEngine;
using System.Collections;

public class Parts : MonoBehaviour {

    Vector3 originalPosition;
     Quaternion originalRotation;



    // Use this for initialization
    void Start()
    {
        // Grab the original local position of the sphere when the app starts.
        originalPosition = this.transform.localPosition;
        originalRotation = this.transform.localRotation;
    }

    // Update is called once per frame
    void Update () {
	
	}

    public void OnReset()
    {
        // Put the sphere back into its original local position.
        this.transform.localPosition = originalPosition;
        this.transform.localRotation = originalRotation;
    }
}
