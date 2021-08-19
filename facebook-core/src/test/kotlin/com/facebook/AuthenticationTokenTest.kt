package com.facebook

import com.facebook.util.common.AuthenticationTokenTestUtil
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest

@PrepareForTest(FacebookSdk::class)
class AuthenticationTokenTest : FacebookPowerMockTestCase() {

  @Before
  fun before() {
    PowerMockito.mockStatic(FacebookSdk::class.java)
    PowerMockito.`when`(FacebookSdk.getApplicationId())
        .thenReturn(AuthenticationTokenTestUtil.APP_ID)
  }

  @Test(expected = IllegalArgumentException::class)
  fun `test empty token throws`() {
    AuthenticationToken("", "nonce")
  }

  @Test(expected = IllegalArgumentException::class)
  fun `test invalid token format`() {
    // Correct format should be [abc.def.ghi]
    AuthenticationToken("abc.def", "nonce")
  }

  @Test
  fun `test AuthenticationToken constructor`() {
    AuthenticationTokenTestUtil.AUTH_TOKEN_CLAIMS_FOR_TEST.toEnCodedString()
    val claimsString = AuthenticationTokenTestUtil.AUTH_TOKEN_CLAIMS_FOR_TEST.toEnCodedString()
    val signatureString = "signature"
    val tokenString =
        "${AuthenticationTokenTestUtil.VALID_HEADER_STRING}.$claimsString.$signatureString"
    val authenticationToken = AuthenticationToken(tokenString, AuthenticationTokenTestUtil.NONCE)
    Assert.assertEquals(tokenString, authenticationToken.token)
    Assert.assertEquals(signatureString, authenticationToken.signature)
  }
}
