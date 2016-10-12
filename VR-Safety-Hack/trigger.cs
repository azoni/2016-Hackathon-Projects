using UnityEngine;
using System.Collections;

public class trigger : MonoBehaviour {

    public static AudioSource myAlarm;

	// Use this for initialization
	void Start () {
        myAlarm = GetComponent<AudioSource>() as AudioSource;
    }
	
    public static void OnTriggerEnter() {
        myAlarm.Play();    
    }
}
