using UnityEngine;
using UnityEngine.UI;
using System.IO;
using System.Text;

public class Timer : MonoBehaviour {

    public Text counterText;
    public float seconds, minutes;
	private int question_num;
	private static float my_height = 0;

    // Use this for initialization
    void Start() {
        counterText = GetComponent<Text>() as Text;
    }
	
	// Update is called once per frame
	void Update() {

		if (my_height < 0.5) { 
			my_height = (float)Camera.main.gameObject.transform.position.y;
		}
        minutes = (int)(Time.time / 60f);
        seconds = (int)(Time.time % 60f);
        counterText.text = minutes.ToString("00") + ":" + seconds.ToString("00");
		if (counterText.text.Equals("00:15")) {
            OpenDialog.PromptUser(0);
		} 
		if (counterText.text.Equals("00:25")) {
			OpenDialog.PromptUser(1);
		} 
		if (counterText.text.Equals("00:35")) {
            trigger.OnTriggerEnter();
			OpenDialog.PromptUser(2);
		} 
        if (counterText.text.Equals("00:55")) {
            OpenDialog.PromptUser(3);
        }
	}
}
