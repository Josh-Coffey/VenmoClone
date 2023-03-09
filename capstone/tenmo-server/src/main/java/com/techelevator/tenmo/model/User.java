package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User {

   /*
    ########################################   Attributes   ##########################################
     */

   private int id;
   private String username;
   @JsonIgnore // prevent from being sent to client
   private String password;
   @JsonIgnore
   private boolean activated;
   private Set<Authority> authorities = new HashSet<>();

   /*
    ########################################   Constructors   #########################################
     */

   public User() { }

   public User(int id, String username, String password, String authorities) {
      this.id = id;
      this.username = username;
      this.password = password;
      if(authorities != null) this.setAuthorities(authorities);
      this.activated = true;
   }


   /*
    ######################################## Getter Methods ##########################################
     */

   public int getId() {
      return id;
   }

   public String getUsername() {
      return username;
   }

   public String getPassword() {
      return password;
   }

   public boolean isActivated() {
      return activated;
   }

   public Set<Authority> getAuthorities() {
      return authorities;
   }

   /*
    ######################################## Setter Methods ##########################################
     */

   public void setId(int id) {
      this.id = id;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public void setActivated(boolean activated) {
      this.activated = activated;
   }

   public void setAuthorities(Set<Authority> authorities) {
      this.authorities = authorities;
   }

   public void setAuthorities(String authorities) {
      String[] roles = authorities.split(",");
      for(String role : roles) {
         this.authorities.add(new Authority("ROLE_" + role));
      }
   }

   /*
    ######################################  Override Methods  ########################################
     */

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      User user = (User) o;
      return id == user.id &&
              activated == user.activated &&
              Objects.equals(username, user.username) &&
              Objects.equals(password, user.password) &&
              Objects.equals(authorities, user.authorities);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, username, password, activated, authorities);
   }

   @Override
   public String toString() {
      return "User{" +
              "id=" + id +
              ", username='" + username + '\'' +
              ", activated=" + activated +
              ", authorities=" + authorities +
              '}';
   }
}
