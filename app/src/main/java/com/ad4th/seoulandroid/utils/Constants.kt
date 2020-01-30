package com.ad4th.seoulandroid.utils

import foundation.icon.icx.data.Address
import foundation.icon.icx.data.IconAmount

object Constants {
    //MAINNET = "https://ctz.solidwallet.io/api/v3";
//TESTNET_BICON = "https://bicon.net.solidwallet.io/api/v3";
    const val URL = "https://bicon.net.solidwallet.io/api/v3"
    // networkId of node 1:mainnet, 2:testnet, 3:bicon
    const val NETWORK_ID = "3"
    // Default address to deploy score.
    val SCORE_INSTALL_ADDRESS = Address("cx0000000000000000000000000000000000000000")
    // Default address to call Governance SCORE API.
    @JvmField
    val GOVERNANCE_ADDRESS = Address("cx0000000000000000000000000000000000000001")
    const val ADDRESS_1 = "hx1000000000000000000000000000000000000000"
    const val PRIVATEKEY_1 = "1000000000000000000000000000000000000000"
    const val ADDRESS_2 = "hx2000000000000000000000000000000000000000"
    const val PRIVATEKEY_2 = "2000000000000000000000000000000000000000"
    const val ADDRESS_3 = "hx3000000000000000000000000000000000000000"
    const val PRIVATEKEY_3 = "3000000000000000000000000000000000000000"
    const val ADDRESS_4 = "hx4000000000000000000000000000000000000000"
    const val PRIVATEKEY_4 = "4000000000000000000000000000000000000000"
    //////////////////////
/*
    개발
    https://dev-evoting-api.ad4th.com/intro
    https://dev-evoting.ad4th.com/dashboard
    운영
    https://evoting-api.ad4th.com/intro
    https://evoting.ad4th.com/dashboard
   */
    const val EVOTING_DEV_URL = "https://dev-evoting-api.ad4th.com"
    const val EVOTING_REAL_URL = "https://evoting-api.ad4th.com"
    const val EVOTING_URL = EVOTING_REAL_URL
    const val EVOTING_INTRO = "/intro"
    const val EVOTING_USER = "/user"
    const val CITIZEN_FRAGMENT = 0x10
    const val CITIZEN_FRAGMENT_BACK = 0x11
    const val CITIZEN_FRAGMENT_MOVE = 0x12
    const val VOTE_FRAGMENT = 0x20
    const val VOTE_FRAGMENT_BACK = 0x21
    const val VOTE_FRAGMENT_MOVE = 0x22
    const val PAY_FRAGMENT = 0x30
    const val PAY_FRAGMENT_BACK = 0x31
    const val PAY_FRAGMENT_MOVE = 0x32
    const val GET_NETWORK_INTRO = 0x40
    const val GET_NETWORK_USER = 0x41
    const val WAIT_TIME: Long = 2000
    //    public static final String product1_qr = "SCOIN:PAY:00001001";
    const val product2_qr = "MDC:PAY:00001002"
    //    public static final String product3_qr = "MIC:PAY:00001003";
    val product1_price = IconAmount.of("3000", 18).toLoop()
    //    public static final BigInteger product2_price = IconAmount.of("3000", 18).toLoop();
//    public static final BigInteger product3_price = IconAmount.of("5000", 18).toLoop();
    const val product1_title = "아이콘백"
    //    public static final String product2_title = "아이콘백";
//    public static final String product3_title = "젤리스티커";
    const val product1_symbol = "ECX"
    //    public static final String product2_symbol = "MDC";
//    public static final String product3_symbol = "MIC";
    const val product1_name = "아이코인"
    //    public static final String product2_name = "명동코인";
//    public static final String product3_name = "K-POP 코인";
    const val product1_company = "아이콘리퍼블릭"
}