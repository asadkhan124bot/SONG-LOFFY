package com.example

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.theme.AmberTierText
import com.example.ui.theme.AvatarSurface
import com.example.ui.theme.BottomNavBackground
import com.example.ui.theme.CardBackgroundDark
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.MissionRewardGreen
import com.example.ui.theme.MissionRewardGreenSoft
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PoppinsFontFamily
import com.example.ui.theme.ScreenBackgroundDark
import com.example.ui.theme.SubBoxBackgroundDark
import com.example.ui.theme.TextMutedGrey
import com.example.ui.theme.TextPrimaryWhite
import com.example.ui.theme.TextSecondaryGrey
import com.example.ui.theme.White12Border
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MissionPayAppScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissionPayAppScreen(viewModel: MissionViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle system Back button when on secondary tabs
    if (state.selectedTab != AppTab.HOME) {
        BackHandler {
            viewModel.selectTab(AppTab.HOME)
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackgroundDark)
    ) {
        val isWideScreen = maxWidth >= 700.dp

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = ScreenBackgroundDark,
            contentWindowInsets = WindowInsets.safeDrawing,
            bottomBar = {
                if (!isWideScreen) {
                    MissionBottomNavigationBar(
                        selectedTab = state.selectedTab,
                        onSelectTab = viewModel::selectTab
                    )
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (isWideScreen) {
                    MissionNavigationRail(
                        selectedTab = state.selectedTab,
                        onSelectTab = viewModel::selectTab
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 580.dp)
                    ) {
                        // Status banner notification when an action completes
                        AnimatedVisibility(
                            visible = state.statusBannerMessage != null,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            state.statusBannerMessage?.let { message ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp, vertical = 8.dp)
                                        .clickable { viewModel.clearBannerMessage() }
                                        .testTag("status_banner"),
                                    color = SubBoxBackgroundDark,
                                    shape = RoundedCornerShape(14.dp),
                                    border = BorderStroke(1.dp, MissionRewardGreen.copy(alpha = 0.5f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.CheckCircle,
                                            contentDescription = "Success",
                                            tint = MissionRewardGreen,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = message,
                                            color = TextPrimaryWhite,
                                            fontFamily = InterFontFamily,
                                            fontSize = 12.5.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }

                        when (state.selectedTab) {
                            AppTab.HOME -> HomeScreenContent(
                                state = state,
                                onWithdrawClick = viewModel::openWithdrawSheet,
                                onNextMissionClick = { viewModel.openMissionSheet(state.nextMission) },
                                onNotificationsClick = viewModel::openNotificationsSheet,
                                onOpenFlutterCodeClick = viewModel::openFlutterCodeSheet,
                                onResetDemoClick = viewModel::resetDemoSnapshot
                            )
                            AppTab.MISSIONS -> MissionsScreenContent(
                                state = state,
                                onSelectCategory = viewModel::selectCategoryFilter,
                                onMissionClick = viewModel::openMissionSheet
                            )
                            AppTab.RANKING -> RankingScreenContent(
                                state = state,
                                rankings = viewModel.getRankings(state)
                            )
                            AppTab.PLANS -> PlansScreenContent(
                                state = state,
                                onTogglePro = viewModel::toggleProMembership
                            )
                        }
                    }
                }
            }
        }

        // Interactive Mission Details & Capture Submission Bottom Sheet
        state.activeMissionForSheet?.let { mission ->
            ModalBottomSheet(
                onDismissRequest = viewModel::closeMissionSheet,
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = CardBackgroundDark,
                contentColor = TextPrimaryWhite,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                MissionSubmissionSheetContent(
                    mission = mission,
                    isProMember = state.isProMember,
                    onSubmitMission = { note ->
                        viewModel.submitAndCompleteMission(mission.id, note)
                    },
                    onUpgradeProClick = {
                        viewModel.closeMissionSheet()
                        viewModel.selectTab(AppTab.PLANS)
                    }
                )
            }
        }

        // Interactive Withdraw Bottom Sheet
        if (state.isWithdrawSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = viewModel::closeWithdrawSheet,
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = CardBackgroundDark,
                contentColor = TextPrimaryWhite,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                WithdrawBottomSheetContent(
                    state = state,
                    onConfirmWithdraw = viewModel::processWithdrawal
                )
            }
        }

        // Interactive Notifications Bottom Sheet
        if (state.isNotificationsSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = viewModel::closeNotificationsSheet,
                containerColor = CardBackgroundDark,
                contentColor = TextPrimaryWhite,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                NotificationsBottomSheetContent(
                    notifications = state.notifications,
                    onResetDemo = {
                        viewModel.closeNotificationsSheet()
                        viewModel.resetDemoSnapshot()
                    }
                )
            }
        }

        // Single-File Flutter lib/main.dart Viewer & Copy Sheet
        if (state.isFlutterCodeSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = viewModel::closeFlutterCodeSheet,
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = CardBackgroundDark,
                contentColor = TextPrimaryWhite,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                FlutterCodeBottomSheetContent(onDismiss = viewModel::closeFlutterCodeSheet)
            }
        }
    }
}

@Composable
fun HomeScreenContent(
    state: MissionUiState,
    onWithdrawClick: () -> Unit,
    onNextMissionClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onOpenFlutterCodeClick: () -> Unit,
    onResetDemoClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    val animatedXpProgress by animateFloatAsState(
        targetValue = state.xpProgress,
        animationSpec = spring(),
        label = "xp_progress_animation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("home_screen_container")
    ) {
        // TOP BAR: Avatar with "H", name "Guest", subtitle "Starter | Bronze Tier" in amber color, notification icon right
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("top_profile_bar"),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar with "H"
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(AvatarSurface)
                    .border(1.2.dp, White12Border, CircleShape)
                    .clickable(onClick = onResetDemoClick)
                    .semantics { contentDescription = "User avatar H" }
                    .testTag("avatar_h_badge"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.avatarInitial,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = TextPrimaryWhite
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Name "Guest" and subtitle "Starter | Bronze Tier" in amber color
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = state.userName,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextPrimaryWhite,
                    modifier = Modifier.testTag("user_name_text")
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = state.tierSubtitle,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = AmberTierText,
                    modifier = Modifier.testTag("user_tier_subtitle")
                )
            }

            // Notification icon right
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(CardBackgroundDark)
                    .border(1.dp, White12Border, RoundedCornerShape(14.dp))
                    .clickable(onClick = onNotificationsClick)
                    .minimumInteractiveComponentSize()
                    .testTag("notification_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.NotificationsNone,
                    contentDescription = stringResource(R.string.cd_notifications),
                    tint = TextPrimaryWhite,
                    modifier = Modifier.size(23.dp)
                )
                if (state.unreadNotificationCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 11.dp, end = 11.dp)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(AmberTierText)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // MIDDLE CARD 1:
        // Background #1F1F2A, rounded 24. Inside: label TOTAL BALANCE (grey small),
        // big text $75.95 bold 38px, two small boxes side by side - "16 Approved" and "$75.95 Lifetime"
        // with background #2C2C38. Bottom full width white button "Withdraw" with black text.
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("middle_card_balance"),
            color = CardBackgroundDark,
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                Text(
                    text = stringResource(R.string.label_total_balance),
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    letterSpacing = 1.1.sp,
                    color = TextSecondaryGrey,
                    modifier = Modifier.testTag("label_total_balance")
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = String.format(Locale.US, "$%.2f", state.totalBalance),
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 38.sp,
                    lineHeight = 44.sp,
                    color = TextPrimaryWhite,
                    modifier = Modifier.testTag("total_balance_value")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Two small boxes side by side - "16 Approved" and "$75.95 Lifetime" with background #2C2C38
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(SubBoxBackgroundDark)
                            .padding(horizontal = 12.dp, vertical = 12.dp)
                            .testTag("approved_stat_box"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${state.approvedCount} Approved",
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.5.sp,
                            color = TextPrimaryWhite,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(SubBoxBackgroundDark)
                            .padding(horizontal = 12.dp, vertical = 12.dp)
                            .testTag("lifetime_stat_box"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = String.format(Locale.US, "$%.2f Lifetime", state.lifetimeEarnings),
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.5.sp,
                            color = TextPrimaryWhite,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Bottom full width white button "Withdraw" with black text
                Button(
                    onClick = onWithdrawClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("withdraw_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = stringResource(R.string.btn_withdraw),
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.5.sp,
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // MIDDLE CARD 2:
        // Background #1F1F2A, Level 4 left, 181/780 XP right, progress bar value 0.23 white color,
        // below "Today: 2/5 missions" and "Streak: 1d 🔥".
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("middle_card_xp"),
            color = CardBackgroundDark,
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Level ${state.level}",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = TextPrimaryWhite,
                        modifier = Modifier.testTag("level_text")
                    )

                    Text(
                        text = "${state.currentXp}/${state.maxXp} XP",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.5.sp,
                        color = TextSecondaryGrey,
                        modifier = Modifier.testTag("xp_counter_text")
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Progress bar value 0.23 white color
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(99.dp))
                        .background(SubBoxBackgroundDark)
                        .testTag("xp_progress_bar")
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedXpProgress)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(99.dp))
                            .background(Color.White)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Today: ${state.todayCompletedMissions}/${state.todayTotalMissions} missions",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        color = TextSecondaryGrey,
                        modifier = Modifier.testTag("today_missions_text")
                    )

                    Text(
                        text = "Streak: ${state.streakDays}d \uD83D\uDD25",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = TextPrimaryWhite,
                        modifier = Modifier.testTag("streak_text")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // BOTTOM CARD:
        // Title "Next Mission", card with border white12, icon eraser,
        // title "Eraser on any surface", green text "+$2.40 • $15.50 with Pro", arrow right.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.section_next_mission),
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp,
                color = TextPrimaryWhite,
                modifier = Modifier.testTag("next_mission_header")
            )

            // Subtle helper pill to view Flutter main.dart code
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(CardBackgroundDark)
                    .border(1.dp, White12Border, RoundedCornerShape(10.dp))
                    .clickable(onClick = onOpenFlutterCodeClick)
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .testTag("flutter_code_button"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Code,
                    contentDescription = "View Flutter main.dart code",
                    tint = TextSecondaryGrey,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "lib/main.dart",
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.5.sp,
                    color = TextSecondaryGrey
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val nextMission = state.nextMission
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .clickable(onClick = onNextMissionClick)
                .testTag("next_mission_card"),
            color = CardBackgroundDark,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.2.dp, White12Border)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Eraser Icon Box
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(SubBoxBackgroundDark),
                    contentAlignment = Alignment.Center
                ) {
                    EraserVectorIcon(modifier = Modifier.size(24.dp))
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = nextMission.title,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.5.sp,
                        color = TextPrimaryWhite,
                        modifier = Modifier.testTag("next_mission_title")
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = nextMission.formattedRewardText,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = MissionRewardGreen,
                        modifier = Modifier.testTag("next_mission_reward")
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = stringResource(R.string.cd_next_mission_arrow),
                    tint = TextSecondaryGrey,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun EraserVectorIcon(
    modifier: Modifier = Modifier,
    tint: Color = TextPrimaryWhite
) {
    val description = stringResource(R.string.cd_eraser_icon)
    Canvas(
        modifier = modifier.semantics { contentDescription = description }
    ) {
        val w = size.width
        val h = size.height
        // Draw custom geometric eraser icon with sleeve + rubber tip + surface line
        rotate(degrees = -35f, pivot = Offset(w * 0.5f, h * 0.46f)) {
            // Main eraser body
            drawRoundRect(
                color = tint,
                topLeft = Offset(w * 0.22f, h * 0.28f),
                size = Size(w * 0.56f, h * 0.34f),
                cornerRadius = CornerRadius(w * 0.10f, w * 0.10f),
                style = Stroke(width = w * 0.085f)
            )
            // Divider band on eraser sleeve
            drawLine(
                color = tint,
                start = Offset(w * 0.46f, h * 0.28f),
                end = Offset(w * 0.46f, h * 0.62f),
                strokeWidth = w * 0.08f
            )
        }
        // Surface baseline under eraser
        drawLine(
            color = tint,
            start = Offset(w * 0.18f, h * 0.82f),
            end = Offset(w * 0.84f, h * 0.82f),
            strokeWidth = w * 0.085f,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun MissionsScreenContent(
    state: MissionUiState,
    onSelectCategory: (String) -> Unit,
    onMissionClick: (MissionModel) -> Unit
) {
    val categories = listOf("All", "Surface", "Lighting", "Hardware", "Objects")
    val filteredMissions = remember(state.missions, state.selectedCategoryFilter) {
        if (state.selectedCategoryFilter == "All") {
            state.missions
        } else {
            state.missions.filter { it.category == state.selectedCategoryFilter }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("missions_tab_list"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Active Missions",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = TextPrimaryWhite
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tap any mission to capture & claim instant USD + XP rewards.",
                    fontFamily = InterFontFamily,
                    fontSize = 13.sp,
                    color = TextSecondaryGrey
                )
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { category ->
                        val selected = state.selectedCategoryFilter == category
                        FilterChip(
                            selected = selected,
                            onClick = { onSelectCategory(category) },
                            label = {
                                Text(
                                    text = category,
                                    fontFamily = InterFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.5.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color.White,
                                selectedLabelColor = Color.Black,
                                containerColor = CardBackgroundDark,
                                labelColor = TextSecondaryGrey
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selected,
                                borderColor = White12Border
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
        }

        items(filteredMissions, key = { it.id }) { mission ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onMissionClick(mission) }
                    .testTag("mission_item_${mission.id}"),
                color = CardBackgroundDark,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.2.dp, White12Border)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(SubBoxBackgroundDark),
                        contentAlignment = Alignment.Center
                    ) {
                        EraserVectorIcon(
                            modifier = Modifier.size(24.dp),
                            tint = if (mission.isCompleted) MissionRewardGreen else TextPrimaryWhite
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = mission.title,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.5.sp,
                                color = TextPrimaryWhite,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            if (mission.isCompleted) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Done",
                                    fontFamily = InterFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = MissionRewardGreen,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(MissionRewardGreenSoft)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = mission.formattedRewardText,
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = MissionRewardGreen
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${mission.durationLabel} • +${mission.xpReward} XP • ${mission.surfaceHint}",
                            fontFamily = InterFontFamily,
                            fontSize = 12.sp,
                            color = TextSecondaryGrey
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Open ${mission.title}",
                        tint = TextSecondaryGrey
                    )
                }
            }
        }
    }
}

@Composable
fun RankingScreenContent(
    state: MissionUiState,
    rankings: List<RankingEntry>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("ranking_tab_list"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CardBackgroundDark,
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "SEASON 4 LEADERBOARD",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.5.sp,
                        letterSpacing = 1.1.sp,
                        color = AmberTierText
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Creator XP & Payout Ranking",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = TextPrimaryWhite
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "You are currently Level ${state.level} (${state.currentXp}/${state.maxXp} XP) with ${state.approvedCount} approved submissions.",
                        fontFamily = InterFontFamily,
                        fontSize = 13.sp,
                        color = TextSecondaryGrey
                    )
                }
            }
        }

        items(rankings, key = { it.rank }) { item ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ranking_row_${item.rank}"),
                color = CardBackgroundDark,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(
                    width = if (item.isCurrentUser) 1.5.dp else 1.dp,
                    color = if (item.isCurrentUser) AmberTierText else White12Border
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "#${item.rank}",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = if (item.rank <= 3) AmberTierText else TextSecondaryGrey,
                        modifier = Modifier.width(34.dp)
                    )

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(SubBoxBackgroundDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.initial,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextPrimaryWhite
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.name,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = TextPrimaryWhite
                        )
                        Text(
                            text = "Level ${item.level} • ${item.tierSubtitle}",
                            fontFamily = InterFontFamily,
                            fontSize = 12.sp,
                            color = if (item.isCurrentUser) AmberTierText else TextSecondaryGrey
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = String.format(Locale.US, "$%.2f", item.lifetimeEarned),
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MissionRewardGreen
                        )
                        Text(
                            text = "${item.xp} XP",
                            fontFamily = InterFontFamily,
                            fontSize = 11.5.sp,
                            color = TextSecondaryGrey
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlansScreenContent(
    state: MissionUiState,
    onTogglePro: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("plans_tab_container"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Membership Plans",
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = TextPrimaryWhite
        )

        // Starter | Bronze Tier Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = CardBackgroundDark,
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(
                width = if (!state.isProMember) 1.5.dp else 1.dp,
                color = if (!state.isProMember) AmberTierText else White12Border
            )
        ) {
            Column(modifier = Modifier.padding(22.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Starter | Bronze Tier",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = AmberTierText
                    )
                    Text(
                        text = "FREE",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TextPrimaryWhite
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "• Standard mission payouts (+\$2.40 per surface capture)\n• Up to 5 daily missions\n• Standard 24h approval queue",
                    fontFamily = InterFontFamily,
                    fontSize = 13.5.sp,
                    lineHeight = 21.sp,
                    color = TextSecondaryGrey
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = { onTogglePro(false) },
                    enabled = state.isProMember,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("select_starter_plan_button"),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, White12Border)
                ) {
                    Text(
                        text = if (!state.isProMember) "Current Active Plan" else "Switch to Starter",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimaryWhite
                    )
                }
            }
        }

        // Pro | Gold Tier Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = CardBackgroundDark,
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(
                width = if (state.isProMember) 1.8.dp else 1.2.dp,
                color = if (state.isProMember) MissionRewardGreen else White12Border
            )
        ) {
            Column(modifier = Modifier.padding(22.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pro | Gold Tier",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MissionRewardGreen
                    )
                    Text(
                        text = "\$15.50 / mission",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MissionRewardGreen
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "• Boosted \$15.50 payout on \"Eraser on any surface\" (6.4x multiplier)\n• Instant automated verification & 0-fee withdrawals\n• Priority access to high-ticket studio missions",
                    fontFamily = InterFontFamily,
                    fontSize = 13.5.sp,
                    lineHeight = 21.sp,
                    color = TextSecondaryGrey
                )
                Spacer(modifier = Modifier.height(18.dp))
                Button(
                    onClick = { onTogglePro(!state.isProMember) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("upgrade_pro_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = if (state.isProMember) "Pro Active (Tap to Reset)" else "Activate Pro Multiplier (\$15.50/mission)",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.5.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun MissionBottomNavigationBar(
    selectedTab: AppTab,
    onSelectTab: (AppTab) -> Unit
) {
    // Bottom Navigation: 4 tabs - Home, Missions, Ranking, Plans with dark background #1A1A23
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("bottom_navigation_bar"),
        color = BottomNavBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            HorizontalDivider(color = White12Border, thickness = 0.8.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppTab.entries.forEach { tab ->
                    val isSelected = selectedTab == tab
                    val icon = when (tab) {
                        AppTab.HOME -> if (isSelected) Icons.Filled.Home else Icons.Outlined.Home
                        AppTab.MISSIONS -> if (isSelected) Icons.Filled.Bolt else Icons.Outlined.Bolt
                        AppTab.RANKING -> if (isSelected) Icons.Filled.EmojiEvents else Icons.Outlined.EmojiEvents
                        AppTab.PLANS -> if (isSelected) Icons.Filled.WorkspacePremium else Icons.Outlined.WorkspacePremium
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { onSelectTab(tab) }
                            .minimumInteractiveComponentSize()
                            .padding(vertical = 8.dp)
                            .testTag("nav_tab_${tab.route}"),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = tab.label,
                            tint = if (isSelected) TextPrimaryWhite else TextSecondaryGrey,
                            modifier = Modifier.size(23.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = tab.label,
                            fontFamily = InterFontFamily,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                            fontSize = 11.5.sp,
                            color = if (isSelected) TextPrimaryWhite else TextSecondaryGrey
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MissionNavigationRail(
    selectedTab: AppTab,
    onSelectTab: (AppTab) -> Unit
) {
    NavigationRail(
        containerColor = BottomNavBackground,
        contentColor = TextPrimaryWhite,
        modifier = Modifier
            .fillMaxHeight()
            .statusBarsPadding()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        AppTab.entries.forEach { tab ->
            val isSelected = selectedTab == tab
            val icon = when (tab) {
                AppTab.HOME -> if (isSelected) Icons.Filled.Home else Icons.Outlined.Home
                AppTab.MISSIONS -> if (isSelected) Icons.Filled.Bolt else Icons.Outlined.Bolt
                AppTab.RANKING -> if (isSelected) Icons.Filled.EmojiEvents else Icons.Outlined.EmojiEvents
                AppTab.PLANS -> if (isSelected) Icons.Filled.WorkspacePremium else Icons.Outlined.WorkspacePremium
            }
            NavigationRailItem(
                selected = isSelected,
                onClick = { onSelectTab(tab) },
                icon = { Icon(imageVector = icon, contentDescription = tab.label) },
                label = {
                    Text(
                        text = tab.label,
                        fontFamily = InterFontFamily,
                        fontSize = 11.5.sp
                    )
                },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    selectedTextColor = TextPrimaryWhite,
                    indicatorColor = Color.White,
                    unselectedIconColor = TextSecondaryGrey,
                    unselectedTextColor = TextSecondaryGrey
                )
            )
        }
    }
}

@Composable
fun MissionSubmissionSheetContent(
    mission: MissionModel,
    isProMember: Boolean,
    onSubmitMission: (String) -> Unit,
    onUpgradeProClick: () -> Unit
) {
    var surfaceDescription by remember { mutableStateOf("Matte walnut desk surface") }
    val activeReward = if (isProMember) mission.proReward else mission.starterReward

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(SubBoxBackgroundDark),
                contentAlignment = Alignment.Center
            ) {
                EraserVectorIcon(modifier = Modifier.size(26.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mission.title,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    color = TextPrimaryWhite
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = mission.formattedRewardText,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.5.sp,
                    color = MissionRewardGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = mission.instructions,
            fontFamily = InterFontFamily,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = TextSecondaryGrey
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = surfaceDescription,
            onValueChange = { surfaceDescription = it },
            label = { Text("Surface / Lighting Tag", fontFamily = InterFontFamily) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("mission_surface_input"),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimaryWhite,
                unfocusedTextColor = TextPrimaryWhite,
                focusedContainerColor = SubBoxBackgroundDark,
                unfocusedContainerColor = SubBoxBackgroundDark,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = White12Border,
                focusedLabelColor = TextSecondaryGrey,
                unfocusedLabelColor = TextSecondaryGrey
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = { onSubmitMission(surfaceDescription) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("submit_mission_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text(
                text = String.format(
                    Locale.US,
                    "Complete Mission (+\$%.2f & +%d XP)",
                    activeReward,
                    mission.xpReward
                ),
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.Black
            )
        }

        if (!isProMember) {
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = onUpgradeProClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, White12Border)
            ) {
                Text(
                    text = String.format(
                        Locale.US,
                        "Unlock \$%.2f payout with Pro",
                        mission.proReward
                    ),
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = MissionRewardGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun WithdrawBottomSheetContent(
    state: MissionUiState,
    onConfirmWithdraw: (Double, String, String) -> Unit
) {
    var amountInput by remember(state.totalBalance) {
        mutableStateOf(String.format(Locale.US, "%.2f", state.totalBalance))
    }
    var selectedMethod by remember { mutableStateOf("PayPal Instant") }
    var destinationInput by remember { mutableStateOf("guest.creator@paypal") }
    val methods = listOf("PayPal Instant", "Bank Transfer", "USDC Wallet")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Withdraw Funds",
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = TextPrimaryWhite
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = String.format(
                Locale.US,
                "Available Balance: \$%.2f • %d Approved Missions",
                state.totalBalance,
                state.approvedCount
            ),
            fontFamily = InterFontFamily,
            fontSize = 13.sp,
            color = TextSecondaryGrey
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            methods.forEach { method ->
                val isSelected = selectedMethod == method
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedMethod = method },
                    label = {
                        Text(
                            text = method,
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.5.sp
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color.White,
                        selectedLabelColor = Color.Black,
                        containerColor = SubBoxBackgroundDark,
                        labelColor = TextPrimaryWhite
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = amountInput,
            onValueChange = { amountInput = it },
            label = { Text("Withdrawal Amount (USD)", fontFamily = InterFontFamily) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("withdraw_amount_input"),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimaryWhite,
                unfocusedTextColor = TextPrimaryWhite,
                focusedContainerColor = SubBoxBackgroundDark,
                unfocusedContainerColor = SubBoxBackgroundDark,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = White12Border
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = destinationInput,
            onValueChange = { destinationInput = it },
            label = { Text("Payout Destination", fontFamily = InterFontFamily) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("withdraw_destination_input"),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimaryWhite,
                unfocusedTextColor = TextPrimaryWhite,
                focusedContainerColor = SubBoxBackgroundDark,
                unfocusedContainerColor = SubBoxBackgroundDark,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = White12Border
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = {
                val parsed = amountInput.toDoubleOrNull() ?: 0.0
                onConfirmWithdraw(parsed, selectedMethod, destinationInput)
            },
            enabled = (amountInput.toDoubleOrNull() ?: 0.0) in 0.01..state.totalBalance,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("confirm_withdraw_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text(
                text = "Withdraw Now",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun NotificationsBottomSheetContent(
    notifications: List<NotificationEntry>,
    onResetDemo: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Activity & Alerts",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = TextPrimaryWhite
            )

            IconButton(
                onClick = onResetDemo,
                modifier = Modifier.testTag("reset_demo_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Reset demo data",
                    tint = AmberTierText
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        notifications.forEach { item ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                color = SubBoxBackgroundDark,
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.title,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = TextPrimaryWhite
                        )
                        Text(
                            text = item.timeAgo,
                            fontFamily = InterFontFamily,
                            fontSize = 11.5.sp,
                            color = TextMutedGrey
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.body,
                        fontFamily = InterFontFamily,
                        fontSize = 12.5.sp,
                        color = TextSecondaryGrey
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun FlutterCodeBottomSheetContent(onDismiss: () -> Unit) {
    val context = LocalContext.current
    var copied by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Flutter Single-File (lib/main.dart)",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = TextPrimaryWhite
                )
                Text(
                    text = "Uses flutter/material.dart + google_fonts (Inter & Poppins)",
                    fontFamily = InterFontFamily,
                    fontSize = 12.sp,
                    color = TextSecondaryGrey
                )
            }

            Button(
                onClick = {
                    val clipboard =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                    clipboard?.setPrimaryClip(
                        ClipData.newPlainText("lib/main.dart", FLUTTER_MAIN_DART_SNIPPET)
                    )
                    copied = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ContentCopy,
                    contentDescription = "Copy Flutter code",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (copied) "Copied!" else "Copy",
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.5.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(ScreenBackgroundDark)
                .border(1.dp, White12Border, RoundedCornerShape(16.dp))
                .padding(14.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = FLUTTER_MAIN_DART_SNIPPET,
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                color = TextPrimaryWhite
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SubBoxBackgroundDark,
                contentColor = TextPrimaryWhite
            )
        ) {
            Text(
                text = "Close",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private const val FLUTTER_MAIN_DART_SNIPPET = """import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

void main() => runApp(const MissionPayApp());

class MissionPayApp extends StatelessWidget {
  const MissionPayApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      theme: ThemeData.dark(useMaterial3: true).copyWith(
        scaffoldBackgroundColor: const Color(0xFF0F0F14),
        textTheme: GoogleFonts.interTextTheme(ThemeData.dark().textTheme),
      ),
      home: const HomeScreen(),
    );
  }
}
// Full runnable single-file code also saved at /lib/main.dart"""
