package xyz.orangej.acmsigninsystemandroid.ui.main.fragments.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.*
import xyz.orangej.acmsigninsystemandroid.SystemApplication
import xyz.orangej.acmsigninsystemandroid.databinding.FragmentDashboardBinding
import xyz.orangej.acmsigninsystemandroid.ui.main.MainActivityViewModel
import kotlin.math.min

/**
 * 显示训练记录的Fragment。
 */
class DashboardFragment : Fragment() {

    companion object {

        /**
         * 之前的异常记录，在重新刷新时最多刷新的个数。
         * 其他异常记录可以在完整刷新时更新。
         */
        const val MAX_REFRESH_NUMBER = 5
    }

    private var _binding: FragmentDashboardBinding? = null

    /**
     * 这个值只在生命周期中onCreateView到onDestroyView有效。
     */
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<MainActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: SwipeRefreshLayout = binding.root

        val adapter = TrainingHistoryAdapter()
        val recyclerView = binding.recyclerDashboard
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.trainingRecords.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        val refreshListener = SwipeRefreshLayout.OnRefreshListener {
            viewModel.viewModelScope.launch {
                val systemApplication = requireActivity().application as SystemApplication
                val dao = systemApplication.getDatabase().userDao()
                val taskList = ArrayList<Deferred<*>>()

                taskList.add(async(Dispatchers.IO) {
                    val maxId = dao.getBiggestRecordId()
                    val list = viewModel.getTrainHistory(requireContext(),systemApplication.session, maxId)
                    for (item in list) {
                        dao.addRecord(item)
                    }
                })
                taskList.add(async(Dispatchers.IO) {
                    val invalidRecords = dao.getUnrecordedRecords()
                    Log.e("size", invalidRecords.size.toString())
                    for (index in 0 until min(MAX_REFRESH_NUMBER, invalidRecords.size)) {
                        val oldRecord = invalidRecords[index]
                        val newRecord = viewModel.getSpecificTrainingHistory(
                            requireContext(),
                            systemApplication.session,
                            oldRecord.id
                        )
                        if (newRecord == null || oldRecord == newRecord) {
                            continue
                        } else {
                            dao.updateRecord(newRecord)
                        }
                    }
                })
                taskList.awaitAll()
                root.isRefreshing = false
            }
        }

        root.setOnRefreshListener(refreshListener)
        lifecycleScope.launchWhenStarted {
            root.isRefreshing = true
            refreshListener.onRefresh()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.trainingRecords.removeObservers(viewLifecycleOwner)
        _binding = null
    }
}