using UnityEngine;
using System.Collections;

public class AppearFromTop : MonoBehaviour {

    Vector3 goTo;


    // Use this for initialization
    void Start()
    {
        goTo = this.transform.position;
        this.transform.Translate(Vector3.up * 50);
        while(this.transform.position.y > goTo.y)
            this.transform.Translate(Vector3.up * -0.001f);
    }
    // Update is called once per frame
        void Update () {
	
	}
}
