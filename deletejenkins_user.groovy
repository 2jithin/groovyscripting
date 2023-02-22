#!/bin/groovy

// ## Incompleted Code!!
/**
Lists all "real" users and shows the timestamp. User that have `LastGrantedAuthoritiesProperty` are "real" because
they loged in at least one time.
WARNING: Timestamp is not the date of the last login. It is the last time granted authorities changed, e.g., user group.
**/
// Current code will work only least one time logged in users

import org.acegisecurity.*
import hudson.model.User
import jenkins.security.*
import java.util.Date
import org.joda.time.DateTime
import jenkins.model.*
import hudson.security.*

def instance = Jenkins.getInstance()
def realm = instance.getSecurityRealm()
def currentDate = new Date()
def cutoffDays = 90
def now = new Date().getTime()
def user = null
def userslist = realm.getAllUsers()
def userfound = false

User.getAll().each{ u ->
  def prop = u.getProperty(LastGrantedAuthoritiesProperty)
  def realUser = false
  if (prop) {
    realUser=true
    
    //def daysSinceLastLogin = (currentDate.getTime() - prop.timestamp) / (1000 * 60 * 60 * 24)
    //println "LAST Login "+daysSinceLastLogin
    
    if (now - prop.timestamp >= cutoffDays * 24 * 60 * 60 * 1000) {
      if (u.getId() == "ab") {
        userfound = true
        user = u.getId()
        user = realm.getUser(u.getId())
        println "The <' ${user} '> is being deleted"
        user.delete()        
      }      
    }
  }
}
if (userfound == false) {
  println "No users are matched with condition or user not at all logged in first time"
}

userslist.each { users ->
  println(users.getId())
}

return