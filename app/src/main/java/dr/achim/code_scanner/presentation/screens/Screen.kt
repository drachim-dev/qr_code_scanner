package dr.achim.code_scanner.presentation.screens

import androidx.annotation.StringRes
import dr.achim.code_scanner.R

/* 
 * QR Code Scanner
 * 
 * Copyright © 2023 BEENIC GmbH. All rights reserved.
 */
enum class Screen(@StringRes val title: Int) {
    Home(R.string.app_name),
    Libraries(R.string.screen_libraries),
    ;

    val route get() = name
}