package code.api

import code.api.util.APIUtil
import code.model.TokenType._
import code.model.{Consumer => OBPConsumer, Token => OBPToken}
import code.model.dataAccess.ResourceUser
import APIUtil.OAuth.{Consumer, Token}
import code.consumer.Consumers
import net.liftweb.util.Helpers._
import net.liftweb.util.Props
import net.liftweb.util.TimeHelpers.TimeSpan
import code.model.User

trait DefaultUsers {

  //create the application
  lazy val testConsumer = Consumers.consumers.vend.createConsumer(Some(randomString(40).toLowerCase), Some(randomString(40).toLowerCase), Some(true), Some("test application"), None, None, None, None, None).get

  val defaultProvider = Props.get("hostname","")

  lazy val consumer = new Consumer (testConsumer.key,testConsumer.secret)

  // create the access token
  val expiration = Props.getInt("token_expiration_weeks", 4)
  lazy val tokenDuration = weeks(expiration)

  lazy val authuser1: ResourceUser = User.createResourceUser(defaultProvider, None, None, None, None).get

  lazy val testToken =
    OBPToken.create.
      tokenType(Access).
      consumerId(testConsumer.id).
      userForeignKey(authuser1.id.toLong).
      key(randomString(40).toLowerCase).
      secret(randomString(40).toLowerCase).
      duration(tokenDuration).
      expirationDate({(now : TimeSpan) + tokenDuration}).
      insertDate(now).
      saveMe

  lazy val token = new Token(testToken.key, testToken.secret)

  // create a user for test purposes
  lazy val authuser2 = User.createResourceUser(defaultProvider, None, None, None, None).get

  //we create an access token for the other user
  lazy val testToken2 =
    OBPToken.create.
      tokenType(Access).
      consumerId(testConsumer.id).
      userForeignKey(authuser2.id.toLong).
      key(randomString(40).toLowerCase).
      secret(randomString(40).toLowerCase).
      duration(tokenDuration).
      expirationDate({(now : TimeSpan) + tokenDuration}).
      insertDate(now).
      saveMe

  lazy val token2 = new Token(testToken2.key, testToken2.secret)

  // create a user for test purposes
  lazy val authuser3 = User.createResourceUser(defaultProvider, None, None, None, None).get

  //we create an access token for the other user
  lazy val testToken3 =
    OBPToken.create.
      tokenType(Access).
      consumerId(testConsumer.id).
      userForeignKey(authuser3.id.toLong).
      key(randomString(40).toLowerCase).
      secret(randomString(40).toLowerCase).
      duration(tokenDuration).
      expirationDate({(now : TimeSpan) + tokenDuration}).
      insertDate(now).
      saveMe

  lazy val token3 = new Token(testToken3.key, testToken3.secret)

  lazy val user1 = Some((consumer, token))
  lazy val user2 = Some((consumer, token2))
  lazy val user3 = Some((consumer, token3))


}
