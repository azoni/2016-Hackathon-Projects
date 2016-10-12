using UnityEngine;
using System.Collections;

public class Boom : MonoBehaviour {

    public GameObject explode;
	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
	    if (this.transform.position.y <= 0)
        {
            explode.SetActive(true);
        }
	}
}
