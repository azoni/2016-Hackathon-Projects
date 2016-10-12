using UnityEngine;
using System.Collections;
using System.IO;
using System.Text;

//Opens dialog for the observer
public class OpenDialog : MonoBehaviour{
    //Holds all the questions and available responses
    static string[] myIndex;
    static StringBuilder myStringBuilder = new StringBuilder();
    private static Rect myWindowRect = new Rect((Screen.width - 200) / 2,
                                (Screen.height - 300) / 2, 300, 300);
    private static string myCurrQuestion;
    private static string myCurrOption1;
    private static string myCurrOption2;
    private static string selectedOption;
    private static bool isVisible = false;
	// Use this for initialization
	void Start() {
        string line = "I see smoke!,Start crawling,Stay standing and walk,What should I look for?, Fire Alarm, Bag, What else should I look for?, Door, My Friend";
        myIndex = line.Split(',');
    }

    public void OnGUI() {
        if (isVisible) {
            myWindowRect = GUI.Window(0, myWindowRect, DialogWindow, "What Should I do?");
        }
    }

    void DialogWindow(int WindowID) {
        GUI.Label(new Rect(5, 20, myWindowRect.width, 20), myCurrQuestion);
        if (GUI.Button(new Rect(5, 270, (myWindowRect.width - 10) / 2, 30), myCurrOption1)) {
            selectedOption = myCurrOption1;
            isVisible = false;
        }
        if (GUI.Button(new Rect(155, 270, (myWindowRect.width - 10) / 2, 30), myCurrOption2)) {
            selectedOption = myCurrOption2;
            isVisible = false;
        }
        myStringBuilder.Append(selectedOption);
    }

    //if you want to have a interlocking story (where nodes point back to each other)
    // then you gotta send do: the question - 1
    // so it goes back to the previous question (you do the wrong response twice, but
    // i dont want to make that better
    // @param the 'wave/round' of question the user is currently on
	public static void PromptUser(int theQuestionLevel) {
        //the value will change depending on the selection. 
        myCurrQuestion = myIndex[(theQuestionLevel * 3) + 0];
        //good response is going to return true
        myCurrOption1 = myIndex[(theQuestionLevel * 3) + 1];
        //bad response is going to return false
        myCurrOption2 = myIndex[(theQuestionLevel * 3) + 2];
        isVisible = true;
    }

    public static string getLastChoice() {
        return selectedOption;
    }

    //Show the path that the student took (log)
    public static string getReview() {
        return myStringBuilder.ToString();
    }
}
