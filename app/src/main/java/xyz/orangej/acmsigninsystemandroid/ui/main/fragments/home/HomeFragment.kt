package xyz.orangej.acmsigninsystemandroid.ui.main.fragments.home

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import xyz.orangej.acmsigninsystemandroid.ui.main.MainActivity

class HomeFragment : Fragment() {

    companion object {

        const val REQUEST_CODE_SCAN_ONE = 12
    }

    private lateinit var permissionRequestCallback: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.permissionRequestCallback =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                if (!it.values.contains(false)) {
                    startScanActivity()
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            HomePage(::onScanButtonClick)
        }
    }

    /**
     * 当按下扫码按钮时触发的回调函数。
     */
    private fun onScanButtonClick() {
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        permissionRequestCallback.launch(permissions)
    }

    /**
     * 启动扫码Activity。只有当权限全部获取后才调用此方法。
     *
     * @see MainActivity.onActivityResult
     */
    private fun startScanActivity() {
        val options = HmsScanAnalyzerOptions.Creator()
            .setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE, HmsScan.DATAMATRIX_SCAN_TYPE).create()
        ScanUtil.startScan(requireActivity(), REQUEST_CODE_SCAN_ONE, options)
    }
}