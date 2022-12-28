package dut.finalproject.s106180042

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.fold
import dagger.hilt.android.lifecycle.HiltViewModel
import dut.finalproject.s106180042.repository.CheckingRepository
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class CheckingViewModel @Inject constructor(
    private val checkingRepository: CheckingRepository
) : ViewModel() {

    private val _textResult: MutableLiveData<String> = MutableLiveData()
    val textResult: LiveData<String> = _textResult
    private val _textOcr: MutableLiveData<String> = MutableLiveData()
    val textOcr: LiveData<String> = _textOcr

    fun checkText(textCheck: String) {
        viewModelScope.launch {
            checkingRepository.startChecking(textCheck).fold(
                success = {
                    _textResult.value = it
                },
                failure = {
                    _textResult.value = it.message.toString()
                }
            )
        }
    }

    fun ocr(uri: Uri) {
        viewModelScope.launch {
            checkingRepository.ocr(uri).fold(
                success = {
                    _textOcr.value = it
                },
                failure = {
                    _textOcr.value = it.message.toString()
                }
            )
        }
    }
}