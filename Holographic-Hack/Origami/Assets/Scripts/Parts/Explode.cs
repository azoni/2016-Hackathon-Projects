using UnityEngine;
using System.Collections;

public class Explode : MonoBehaviour {

  
	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
 
	}

    void Booom()
    {

    }

    void OnTriggerEnter(Collider other)
    {
       
        if (other.GetComponent<Rigidbody>())
        {
            print(other.transform.position - this.transform.position);
            other.GetComponent<Rigidbody>().AddForceAtPosition((other.transform.position - this.transform.position).normalized, this.transform.position, ForceMode.Force); 
        }
    }
}
