package dr.achim.code_scanner.presentation.screens.history

import dr.achim.code_scanner.domain.model.Code

sealed interface HistoryScreenState {
    data object Loading : HistoryScreenState
    data object Error : HistoryScreenState
    data class Success(
        val categories: List<Category>,
        val onDelete: (codes: List<Code>) -> Unit,
        val onDeleteAll: () -> Unit
    ) : HistoryScreenState
}