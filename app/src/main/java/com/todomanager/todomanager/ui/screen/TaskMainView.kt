package com.todomanager.todomanager.ui.screen

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import coil.compose.rememberAsyncImagePainter
import com.todomanager.todomanager.R
import com.todomanager.todomanager.constant.Destination.TASK_ADD
import com.todomanager.todomanager.constant.Destination.TASK_EDIT
import com.todomanager.todomanager.constant.NavArgKey
import com.todomanager.todomanager.dto.Task
import com.todomanager.todomanager.ui.theme.B1
import com.todomanager.todomanager.ui.theme.G2
import com.todomanager.todomanager.ui.theme.G4
import com.todomanager.todomanager.ui.theme.Typography
import kotlin.math.roundToInt

class TaskMainView {

    @Composable
    fun TaskMainScreen(
        navController: NavHostController,
        registerViewModel: RegisterViewModel,
        taskViewModel: TaskViewModel
    ) {
        LaunchedEffect(Unit) {
            registerViewModel.getProfile()
            taskViewModel.loadTaskList()
        }

        val profile by registerViewModel.profileState.collectAsState()
        val painter = rememberAsyncImagePainter(profile.uri)
        val taskList by taskViewModel.taskListState.collectAsState()

        Box(modifier = Modifier.fillMaxSize()) {
            if (taskList.isEmpty()) {
                val modifier = Modifier.align(Alignment.Center)
                EmptyTask(modifier)
            }
            Column {
                Spacer(modifier = Modifier.height(35.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(top = 10.dp, start = 30.dp),
                        text = stringResource(id = R.string.todo_list),
                        style = Typography.headlineLarge,
                        color = G2
                    )
                    Image(
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .size(60.dp)
                            .clip(CircleShape),
                        painter = painter,
                        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                            setToSaturation(
                                0f
                            )
                        }),
                        contentScale = ContentScale.Crop,
                        contentDescription = "photo_profile"
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
                TaskList(
                    taskList
                ) { index, direction ->
                    if (direction == TODO_COMPLETE) {
                        taskViewModel.removeTask(taskList[index].id)
                    } else if (direction == TODO_EDIT) {
                        navController.navigate("$TASK_EDIT?${NavArgKey.TASK_ID_EDIT_KEY}=${taskList[index].id}")
                    }
                }
            }
            FloatingButton(
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 45.dp, end = 20.dp)
            ) {
                navController.navigate(TASK_ADD)
            }
        }
    }

    @Composable
    fun FloatingButton(modifier: Modifier, onClick: () -> Unit) {
        Box(
            modifier = modifier
                .size(55.dp)
                .clip(CircleShape)
                .background(B1)
                .clickable { onClick() }
        ) {
            Image(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center),
                painter = painterResource(id = R.drawable.ic_add_todo),
                contentDescription = ""
            )
        }
    }

    @Composable
    fun EmptyTask(modifier: Modifier) {
        Column(modifier = modifier) {
            Image(
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.ic_empty_box),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(id = R.string.empty_todo),
                textAlign = TextAlign.Center,
                style = Typography.headlineSmall,
                color = G4
            )
        }
    }

    @Composable
    fun TaskList(todoList: List<Task>, onSwiped: (Int, String) -> Unit) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(todoList.size) { index ->
                if (index == 0) {
                    Spacer(modifier = Modifier.height(20.dp))
                }
                TaskRow(index = index, todo = todoList[index], onSwiped)
                if (index == todoList.size - 1) {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }

    @OptIn(ExperimentalWearMaterialApi::class)
    @Composable
    fun TaskRow(index: Int, todo: Task, onSwiped: (Int, String) -> Unit) {
        val swipeableState = rememberSwipeableState(initialValue = 0)
        val anchorSize = 80.dp
        val sizePx = with(LocalDensity.current) { anchorSize.toPx() }
        val modifier = Modifier
            .swipeable(
                state = swipeableState,
                anchors = mapOf(0f to 0, sizePx to 1, -sizePx to 2),
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Horizontal
            )
            .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }

        LaunchedEffect(!swipeableState.isAnimationRunning) {
            if (swipeableState.offset.value == sizePx) {
                swipeableState.animateTo(0, tween(200, 0))
                onSwiped(index, TODO_COMPLETE)
            } else if (swipeableState.offset.value == -sizePx) {
                swipeableState.animateTo(0, tween(200, 0))
                onSwiped(index, TODO_EDIT)
            }
        }

        Box(modifier = Modifier.wrapContentSize()) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(60.dp)
                    .background(B1),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(start = 20.dp),
                    painter = painterResource(id = R.drawable.ic_complete_todo),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    modifier = Modifier
                        .padding(end = 20.dp),
                    painter = painterResource(id = R.drawable.ic_edit_todo),
                    contentDescription = ""
                )
            }
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 20.dp)
                    .border(width = 2.dp, color = G2),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Row(
                    Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 15.dp)
                            .weight(1.5f),
                        text = todo.name,
                        style = Typography.displayLarge,
                        color = G2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        onTextLayout = { textLayoutResult ->
                            if (textLayoutResult.hasVisualOverflow) {
                                val lineEndIndex = textLayoutResult.getLineEnd(
                                    lineIndex = 0,
                                    visibleEnd = true
                                )
                                todo.name = todo.name.substring(0, lineEndIndex) + "···"
                            }
                        }
                    )
                    Text(
                        modifier = Modifier
                            .padding(end = 15.dp)
                            .weight(1f),
                        text = todo.taskDate.toString(),
                        style = Typography.displaySmall,
                        color = G2,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }

    companion object {
        const val TODO_COMPLETE = "todo_complete"
        const val TODO_EDIT = "todo_edit"
    }
}