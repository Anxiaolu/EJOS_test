package cn.edu.sdut.softlab.entity;

import java.io.Serializable;
import javax.enterprise.inject.Alternative;
import javax.persistence.*;


/**
 * The persistent class for the admin database table.
 * 
 */
@Entity
@Table(name="admin")
@Alternative
@NamedQueries({@NamedQuery(name="Admin.findAll", query="SELECT a FROM Admin a"),
                @NamedQuery(name = "Admin.findById",query = "SELECT a FROM Admin a WHERE a.id = :id"),
                @NamedQuery(name = "Admin.findByIdAndPassword",query = "SELECT a FROM Admin a WHERE a.id = :id and a.password = :password"),
                @NamedQuery(name = "Admin.findByNameAndPassword",query = "SELECT a FROM Admin a WHERE a.name = :name and a.password = :password")
})
public class Admin implements Serializable,Level{
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ADMIN_ID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ADMIN_ID_GENERATOR")
	private int id;

	private String email;

	private String name;

	private String password;

	public Admin() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}