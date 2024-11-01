package dr.achim.code_scanner.presentation.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dr.achim.code_scanner.common.DateTimeFormat
import dr.achim.code_scanner.common.DateTimeFormatter
import dr.achim.code_scanner.common.formatWith
import dr.achim.code_scanner.domain.model.Code
import dr.achim.code_scanner.domain.usecase.code.DeleteAllCodesFromHistory
import dr.achim.code_scanner.domain.usecase.code.DeleteCodesFromHistory
import dr.achim.code_scanner.domain.usecase.code.GetCodesFromHistory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    getCodesFromHistory: GetCodesFromHistory,
    private val deleteCodesFromHistory: DeleteCodesFromHistory,
    private val deleteAllCodesFromHistory: DeleteAllCodesFromHistory,
    private val dateTimeFormatter: DateTimeFormatter
) : ViewModel() {

    val viewState: StateFlow<HistoryScreenState> =
        getCodesFromHistory()
            .map<_, HistoryScreenState> { codes ->
                val categories = codes
                    .sortedByDescending { it.created }
                    .groupBy {
                        it.created.formatWith(dateTimeFormatter) {
                            format = DateTimeFormat.Pattern.MonthYear
                        }
                    }
                    .map { Category(it.key, it.value) }

                HistoryScreenState.Success(
                    categories = categories,
                    onDelete = ::deleteFromHistory,
                    onDeleteAll = ::deleteAllFromHistory
                )
            }
            .onStart { emit(HistoryScreenState.Loading) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HistoryScreenState.Loading,
            )

    private fun deleteFromHistory(codes: List<Code>) {
        viewModelScope.launch {
            deleteCodesFromHistory(codes)
        }
    }

    private fun deleteAllFromHistory() {
        viewModelScope.launch {
            deleteAllCodesFromHistory()
        }
    }
}