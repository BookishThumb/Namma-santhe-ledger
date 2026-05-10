package com.nammasanthe.ledger.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nammasanthe.ledger.ui.customer.home.CustomerHomeScreen
import com.nammasanthe.ledger.ui.customer.login.CustomerLoginScreen
import com.nammasanthe.ledger.ui.vendor.addcustomer.AddCustomerScreen
import com.nammasanthe.ledger.ui.vendor.addtransaction.AddTransactionScreen
import com.nammasanthe.ledger.ui.vendor.customerdetail.CustomerDetailScreen
import com.nammasanthe.ledger.ui.vendor.dailysummary.DailySummaryScreen
import com.nammasanthe.ledger.ui.vendor.home.VendorHomeScreen
import com.nammasanthe.ledger.ui.vendor.login.VendorLoginScreen
import com.nammasanthe.ledger.ui.welcome.WelcomeScreen

object Routes {
    const val WELCOME           = "welcome"
    const val VENDOR_LOGIN      = "vendor/login"
    const val VENDOR_HOME       = "vendor/home"
    const val ADD_CUSTOMER      = "vendor/add-customer"
    const val ADD_TRANSACTION   = "vendor/add-transaction"
    const val DAILY_SUMMARY     = "vendor/daily-summary"
    const val CUSTOMER_DETAIL   = "vendor/customer/{customerId}"
    const val CUSTOMER_LOGIN    = "customer/login"
    const val CUSTOMER_HOME     = "customer/home/{customerCode}"

    fun customerDetail(id: String) = "vendor/customer/$id"
    fun customerHome(code: String) = "customer/home/$code"
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.WELCOME) {

        composable(Routes.WELCOME) {
            WelcomeScreen(
                onVendorClick   = { navController.navigate(Routes.VENDOR_LOGIN) },
                onCustomerClick = { navController.navigate(Routes.CUSTOMER_LOGIN) }
            )
        }

        composable(Routes.VENDOR_LOGIN) {
            VendorLoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.VENDOR_HOME) {
                        popUpTo(Routes.WELCOME) { inclusive = false }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.VENDOR_HOME) {
            VendorHomeScreen(
                onAddCustomer    = { navController.navigate(Routes.ADD_CUSTOMER) },
                onAddTransaction = { navController.navigate(Routes.ADD_TRANSACTION) },
                onCustomerClick  = { navController.navigate(Routes.customerDetail(it)) },
                onDailySummary   = { navController.navigate(Routes.DAILY_SUMMARY) },
                onLogout         = {
                    navController.navigate(Routes.WELCOME) { popUpTo(0) { inclusive = true } }
                }
            )
        }

        composable(Routes.ADD_CUSTOMER) {
            AddCustomerScreen(onDone = { navController.popBackStack() }, onBack = { navController.popBackStack() })
        }

        composable(Routes.ADD_TRANSACTION) {
            AddTransactionScreen(onDone = { navController.popBackStack() }, onBack = { navController.popBackStack() })
        }

        composable(Routes.DAILY_SUMMARY) {
            DailySummaryScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Routes.CUSTOMER_DETAIL,
            arguments = listOf(navArgument("customerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getString("customerId") ?: return@composable
            CustomerDetailScreen(customerId = customerId, onBack = { navController.popBackStack() })
        }

        composable(Routes.CUSTOMER_LOGIN) {
            CustomerLoginScreen(
                onLoginSuccess = { code ->
                    navController.navigate(Routes.customerHome(code)) {
                        popUpTo(Routes.WELCOME) { inclusive = false }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.CUSTOMER_HOME,
            arguments = listOf(navArgument("customerCode") { type = NavType.StringType })
        ) { backStackEntry ->
            val code = backStackEntry.arguments?.getString("customerCode") ?: ""
            CustomerHomeScreen(
                customerCode = code,
                onLogout = {
                    navController.navigate(Routes.WELCOME) { popUpTo(0) { inclusive = true } }
                }
            )
        }
    }
}
