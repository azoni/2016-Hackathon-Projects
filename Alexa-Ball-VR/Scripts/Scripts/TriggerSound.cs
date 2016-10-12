using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class TriggerSound : MonoBehaviour {

	[SerializeField]
	private List<AudioClip> audioClips;

	[SerializeField]
	private AudioSource audioSource;

	void OnTriggerEnter(Collider other) {

		audioSource.transform.position = other.transform.position;

		// Adjust volume to match velocity

		audioSource.volume = Mathf.Clamp01 (other.GetComponent<Rigidbody>().velocity.magnitude * 0.2f);

		audioSource.pitch = Random.Range (0.98f, 1.02f);

		audioSource.clip = audioClips [Random.Range (0, audioClips.Count)];

		audioSource.Play ();
	}
}
