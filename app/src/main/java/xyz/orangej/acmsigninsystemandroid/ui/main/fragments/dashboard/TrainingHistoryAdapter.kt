package xyz.orangej.acmsigninsystemandroid.ui.main.fragments.dashboard

import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.orangej.acmsigninsystemandroid.data.user.TrainingRecord

/**
 * 显示训练记录的RecyclerView的适配器。
 */
class TrainingHistoryAdapter :
    ListAdapter<TrainingRecord, TrainingHistoryAdapter.ViewHolder>(DifferenceCallback) {

    class ViewHolder(val rootView: ComposeView) : RecyclerView.ViewHolder(rootView) {

        init {
            rootView.setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
        }
    }

    /**
     * 判断数据对象是否相同的相关工具对象。
     */
    object DifferenceCallback : DiffUtil.ItemCallback<TrainingRecord>() {

        override fun areItemsTheSame(oldItem: TrainingRecord, newItem: TrainingRecord): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TrainingRecord, newItem: TrainingRecord) = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ComposeView(parent.context))
    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.rootView.disposeComposition()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.rootView.setContent {
            TrainingHistoryCard(data = getItem(position))
        }
    }
}