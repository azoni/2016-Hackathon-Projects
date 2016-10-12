using UnityEngine;

public class TapToPlaceParent : MonoBehaviour
{
    public GameObject building;
    bool placing = true;

    void Start()
    {
        SpatialMapping.Instance.DrawVisualMeshes = true;
        //GetComponent<Raising>().Raise();
        //building.SetActive(true);
    }
    // Called by GazeGestureManager when the user performs a Select gesture
    void OnSelect()
    {

        // On each Select gesture, toggle whether the user is in placing mode.
        Build();
    }

    void Build()
    {
        SpatialMapping.Instance.DrawVisualMeshes = false;
        GetComponent<Raising>().Raise();
        building.SetActive(true);
        placing = false;


    }

    // Update is called once per frame
    void Update()
    {
        // If the user is in placing mode,
        // update the placement to match the user's gaze.

        if (placing)
        {
            // Do a raycast into the world that will only hit the Spatial Mapping mesh.
            var headPosition = Camera.main.transform.position;
            var gazeDirection = Camera.main.transform.forward;

            RaycastHit hitInfo;
            if (Physics.Raycast(headPosition, gazeDirection, out hitInfo,
                30.0f, SpatialMapping.PhysicsRaycastMask))
            {

                // Move this object's parent object to
                // where the raycast hit the Spatial Mapping mesh.
                this.transform.parent.position = hitInfo.point;

                // Rotate this object's parent object to face the user.
                Quaternion toQuat = Camera.main.transform.localRotation;
                toQuat.x = 0;
                toQuat.z = 0;
                this.transform.parent.rotation = toQuat;


            }


        }
    }
}