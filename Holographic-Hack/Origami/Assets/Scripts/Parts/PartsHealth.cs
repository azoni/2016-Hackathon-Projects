using UnityEngine;
using System.Collections;

public class PartsHealth : MonoBehaviour {

    public float health;
    public GameObject smaller;

    float currentHealth;
    Rigidbody rigid;
    Vector3 originScale;


    // Use this for initialization
    void Start () {
        rigid = GetComponent<Rigidbody>();
        originScale = this.transform.localScale;
        currentHealth = health;
    }
	
	// Update is called once per frame
	void Update () {
	
	}

    void OnCollisionEnter(Collision other)
    {
        if (other.rigidbody != null)
        {
            float momentum = rigid.mass * (rigid.velocity - other.rigidbody.velocity).magnitude;
            Damage(momentum);
            PartsHealth otherCube = other.gameObject.GetComponent<PartsHealth>();
            if (otherCube != null)
            {
                otherCube.Damage(momentum);
            }

            if (other.gameObject.tag == "Balls") {
                Damage(other.gameObject.GetComponent<ShootTheBall>().damage);
            }

        }
        else
        {
            float momentum = rigid.mass * rigid.velocity.magnitude;
            Damage(momentum);
        }
    }

    void Damage(float damage)
    {
        currentHealth -= damage;
        print(currentHealth);
        if (currentHealth <= 0)
        {
            Destroy(gameObject);
        } else if (currentHealth <= 0.6 * health && smaller != null)
        {
            Mesh mesh = GetComponent<MeshFilter>().mesh;
            Destroy(gameObject);
            Instantiate(smaller, this.transform.position, this.transform.rotation);
    
            Instantiate(smaller, new Vector3( this.transform.position.x, this.transform.position.y + mesh.bounds.size.y / 2, this.transform.position.z), this.transform.rotation);

        } 
    }
}
