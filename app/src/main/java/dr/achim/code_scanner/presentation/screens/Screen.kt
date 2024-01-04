package dr.achim.code_scanner.presentation.screens

/* 
 * QR Code Scanner
 * 
 * Copyright © 2023 BEENIC GmbH. All rights reserved.
 */
sealed class Screen(val route: String) {
    data object Home : Screen("home_screen")
    data object Libraries : Screen("libraries_screen")
}