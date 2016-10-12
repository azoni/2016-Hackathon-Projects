using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class ImpactSound : MonoBehaviour {

	[SerializeField]
	private List<AudioClip> audioClips;

	[SerializeField]
	private AudioSource audioSource;

	void OnCollisionEnter(Collision other) {
	
		audioSource.transform.position = other.contacts [0].point;

		// Adjust volume to match velocity

		audioSource.volume = Mathf.Clamp01 (other.relativeVelocity.magnitude * 0.2f);

		audioSource.pitch = Random.Range (0.98f, 1.02f);

		audioSource.clip = audioClips [Random.Range (0, audioClips.Count)];

		audioSource.Play ();
	}
}
