package com.example.quickpoll.ui.common.shared_components

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

internal fun LazyListState.reachedBottom(buffer: Int = 1): Boolean {
    val lastVisibleItem = this.layoutInfo.visibleItemsInfo.lastOrNull()
    return lastVisibleItem?.index != 0 && lastVisibleItem?.index == this.layoutInfo.totalItemsCount - buffer
}

@Composable
internal fun <T> EndlessLazyColumn(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    refreshing: Boolean = false,
    listState: LazyListState = rememberLazyListState(),
    items: List<T>,
    itemKey: (T) -> Any,
    itemContent: @Composable (T) -> Unit,
    loadingItem: @Composable () -> Unit,
    loadMore: () -> Unit,
    listEndContent: @Composable (() -> Unit)? = null,
    listHeaderContent: @Composable (() -> Unit)? = null
) {

    val reachedBottom by remember { derivedStateOf { listState.reachedBottom() } }

    // load more if scrolled to bottom
    LaunchedEffect(reachedBottom) {
        if (reachedBottom && !loading && !refreshing) {
            loadMore()
        }
    }

    LazyColumn(modifier = modifier, state = listState) {
        if (listHeaderContent != null) {
            item {
                listHeaderContent()
            }
        }
        items(
            items = items,
            key = { item: T -> itemKey(item) },
        ) { item ->
            itemContent(item)
        }
        if (loading) {
            item {
                loadingItem()
            }
        }
        if (listEndContent != null) {
            item {
                listEndContent()
            }
        }
    }
}