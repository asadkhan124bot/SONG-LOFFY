package com.example

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

enum class AppTab(val route: String, val label: String) {
    HOME("home", "Home"),
    MISSIONS("missions", "Missions"),
    RANKING("ranking", "Ranking"),
    PLANS("plans", "Plans")
}

enum class MissionIconType {
    ERASER,
    COFFEE_MUG,
    KEYBOARD_KEYCAP,
    METALLIC_COIN,
    SNEAKER_SOLE,
    WATCH_DIAL
}

data class MissionModel(
    val id: String,
    val title: String,
    val category: String,
    val starterReward: Double,
    val proReward: Double,
    val xpReward: Int,
    val durationLabel: String,
    val surfaceHint: String,
    val instructions: String,
    val iconType: MissionIconType,
    val isCompleted: Boolean = false
) {
    val formattedRewardText: String
        get() = String.format(
            Locale.US,
            "+$%.2f • $%.2f with Pro",
            starterReward,
            proReward
        )
}

data class WithdrawalRecord(
    val id: String,
    val amount: Double,
    val method: String,
    val destination: String,
    val timestampLabel: String,
    val statusLabel: String = "Completed"
)

data class RankingEntry(
    val rank: Int,
    val initial: String,
    val name: String,
    val tierSubtitle: String,
    val level: Int,
    val xp: Int,
    val lifetimeEarned: Double,
    val isCurrentUser: Boolean = false
)

data class NotificationEntry(
    val id: String,
    val title: String,
    val body: String,
    val timeAgo: String,
    val isUnread: Boolean
)

data class MissionUiState(
    val selectedTab: AppTab = AppTab.HOME,
    val userName: String = "Guest",
    val avatarInitial: String = "H",
    val isProMember: Boolean = false,
    val totalBalance: Double = 75.95,
    val approvedCount: Int = 16,
    val lifetimeEarnings: Double = 75.95,
    val level: Int = 4,
    val currentXp: Int = 181,
    val maxXp: Int = 780,
    val todayCompletedMissions: Int = 2,
    val todayTotalMissions: Int = 5,
    val streakDays: Int = 1,
    val selectedCategoryFilter: String = "All",
    val missions: List<MissionModel> = defaultMissions(),
    val withdrawalHistory: List<WithdrawalRecord> = defaultWithdrawals(),
    val notifications: List<NotificationEntry> = defaultNotifications(),
    val activeMissionForSheet: MissionModel? = null,
    val isWithdrawSheetOpen: Boolean = false,
    val isNotificationsSheetOpen: Boolean = false,
    val isFlutterCodeSheetOpen: Boolean = false,
    val statusBannerMessage: String? = null
) {
    val tierSubtitle: String
        get() = if (isProMember) "Pro | Gold Tier" else "Starter | Bronze Tier"

    val xpProgress: Float
        get() = if (maxXp > 0) (currentXp.toFloat() / maxXp.toFloat()).coerceIn(0f, 1f) else 0.23f

    val nextMission: MissionModel
        get() = missions.firstOrNull { !it.isCompleted } ?: missions.first()

    val unreadNotificationCount: Int
        get() = notifications.count { it.isUnread }
}

private fun defaultMissions(): List<MissionModel> = listOf(
    MissionModel(
        id = "eraser_surface",
        title = "Eraser on any surface",
        category = "Surface",
        starterReward = 2.40,
        proReward = 15.50,
        xpReward = 65,
        durationLabel = "15 sec clip",
        surfaceHint = "Wood, desk mat, paper, or stone",
        instructions = "Place a standard rubber or vinyl eraser on any flat surface with clear texture and record a steady 15-second top-down pan.",
        iconType = MissionIconType.ERASER,
        isCompleted = false
    ),
    MissionModel(
        id = "coffee_mug_warm",
        title = "Ceramic mug on countertop",
        category = "Lighting",
        starterReward = 3.10,
        proReward = 18.00,
        xpReward = 80,
        durationLabel = "20 sec clip",
        surfaceHint = "Kitchen counter or cafe table",
        instructions = "Capture a ceramic mug under indoor warm lighting showing subtle rim reflections and handle geometry.",
        iconType = MissionIconType.COFFEE_MUG,
        isCompleted = false
    ),
    MissionModel(
        id = "keyboard_keycap",
        title = "Mechanical keyboard keycap",
        category = "Hardware",
        starterReward = 2.85,
        proReward = 16.40,
        xpReward = 75,
        durationLabel = "15 sec clip",
        surfaceHint = "Desk setup, close-up macro",
        instructions = "Focus on clean keyboard keycaps from a 45-degree angle and slowly tilt to reveal surface texture.",
        iconType = MissionIconType.KEYBOARD_KEYCAP,
        isCompleted = false
    ),
    MissionModel(
        id = "metallic_coin_wood",
        title = "Metallic coin on wood grain",
        category = "Surface",
        starterReward = 2.20,
        proReward = 14.00,
        xpReward = 55,
        durationLabel = "12 sec clip",
        surfaceHint = "Natural wood desk or table",
        instructions = "Place a single coin on wooden grain and move the camera in a smooth semi-circle to capture specular highlights.",
        iconType = MissionIconType.METALLIC_COIN,
        isCompleted = false
    ),
    MissionModel(
        id = "sneaker_sole_texture",
        title = "Sneaker outsole tread pattern",
        category = "Objects",
        starterReward = 3.60,
        proReward = 21.00,
        xpReward = 95,
        durationLabel = "25 sec clip",
        surfaceHint = "Clean indoor floor or bench",
        instructions = "Capture high-contrast rubber tread details across the heel and forefoot under even illumination.",
        iconType = MissionIconType.SNEAKER_SOLE,
        isCompleted = false
    ),
    MissionModel(
        id = "watch_dial_reflection",
        title = "Watch crystal glass reflection",
        category = "Lighting",
        starterReward = 4.20,
        proReward = 24.50,
        xpReward = 110,
        durationLabel = "20 sec clip",
        surfaceHint = "Window light or desk lamp",
        instructions = "Record the dial and glass reflection of an analog or digital wristwatch as light shifts across the bezel.",
        iconType = MissionIconType.WATCH_DIAL,
        isCompleted = false
    )
)

private fun defaultWithdrawals(): List<WithdrawalRecord> = listOf(
    WithdrawalRecord(
        id = "w_102",
        amount = 42.50,
        method = "PayPal Instant",
        destination = "guest.creator@paypal",
        timestampLabel = "Oct 01 • 09:14 AM",
        statusLabel = "Settled"
    ),
    WithdrawalRecord(
        id = "w_101",
        amount = 33.45,
        method = "USDC Wallet",
        destination = "0x71C...9A2B",
        timestampLabel = "Sep 28 • 06:40 PM",
        statusLabel = "Settled"
    )
)

private fun defaultNotifications(): List<NotificationEntry> = listOf(
    NotificationEntry(
        id = "n1",
        title = "Mission Approved (+\$2.40)",
        body = "Your capture #16 passed quality check and \$2.40 was added to your balance.",
        timeAgo = "14m ago",
        isUnread = true
    ),
    NotificationEntry(
        id = "n2",
        title = "Next Mission Unlocked",
        body = "\"Eraser on any surface\" is now available with +\$2.40 (or \$15.50 with Pro).",
        timeAgo = "1h ago",
        isUnread = true
    ),
    NotificationEntry(
        id = "n3",
        title = "Streak Active: 1d 🔥",
        body = "Complete 3 more missions today to hit your 5/5 daily bonus target!",
        timeAgo = "3h ago",
        isUnread = false
    )
)

class MissionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MissionUiState())
    val uiState: StateFlow<MissionUiState> = _uiState.asStateFlow()

    fun selectTab(tab: AppTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun selectCategoryFilter(category: String) {
        _uiState.update { it.copy(selectedCategoryFilter = category) }
    }

    fun openMissionSheet(mission: MissionModel) {
        _uiState.update { it.copy(activeMissionForSheet = mission) }
    }

    fun closeMissionSheet() {
        _uiState.update { it.copy(activeMissionForSheet = null) }
    }

    fun openWithdrawSheet() {
        _uiState.update { it.copy(isWithdrawSheetOpen = true) }
    }

    fun closeWithdrawSheet() {
        _uiState.update { it.copy(isWithdrawSheetOpen = false) }
    }

    fun openNotificationsSheet() {
        _uiState.update { state ->
            state.copy(
                isNotificationsSheetOpen = true,
                notifications = state.notifications.map { it.copy(isUnread = false) }
            )
        }
    }

    fun closeNotificationsSheet() {
        _uiState.update { it.copy(isNotificationsSheetOpen = false) }
    }

    fun openFlutterCodeSheet() {
        _uiState.update { it.copy(isFlutterCodeSheetOpen = true) }
    }

    fun closeFlutterCodeSheet() {
        _uiState.update { it.copy(isFlutterCodeSheetOpen = false) }
    }

    fun clearBannerMessage() {
        _uiState.update { it.copy(statusBannerMessage = null) }
    }

    fun submitAndCompleteMission(missionId: String, surfaceNote: String) {
        _uiState.update { state ->
            val target = state.missions.find { it.id == missionId } ?: return@update state
            val payout = if (state.isProMember) target.proReward else target.starterReward
            val newBalance = state.totalBalance + payout
            val newLifetime = state.lifetimeEarnings + payout
            val newApproved = state.approvedCount + 1
            val newToday = (state.todayCompletedMissions + 1).coerceAtMost(state.todayTotalMissions)

            var updatedLevel = state.level
            var updatedXp = state.currentXp + target.xpReward
            if (updatedXp >= state.maxXp) {
                updatedLevel += 1
                updatedXp -= state.maxXp
            }

            val updatedMissions = state.missions.map {
                if (it.id == missionId) it.copy(isCompleted = true) else it
            }

            val noteSuffix = if (surfaceNote.isNotBlank()) " ($surfaceNote)" else ""
            val newNotification = NotificationEntry(
                id = "n_${System.currentTimeMillis()}",
                title = String.format(Locale.US, "Approved: %s (+\$%.2f)", target.title, payout),
                body = "Verified capture$noteSuffix • +${target.xpReward} XP credited.",
                timeAgo = "Just now",
                isUnread = false
            )

            state.copy(
                totalBalance = newBalance,
                lifetimeEarnings = newLifetime,
                approvedCount = newApproved,
                level = updatedLevel,
                currentXp = updatedXp,
                todayCompletedMissions = newToday,
                missions = updatedMissions,
                notifications = listOf(newNotification) + state.notifications,
                activeMissionForSheet = null,
                statusBannerMessage = String.format(
                    Locale.US,
                    "Mission approved! +\$%.2f & +%d XP added.",
                    payout,
                    target.xpReward
                )
            )
        }
    }

    fun processWithdrawal(amount: Double, method: String, destination: String) {
        _uiState.update { state ->
            val validAmount = amount.coerceIn(0.0, state.totalBalance)
            if (validAmount <= 0.0) return@update state

            val newRecord = WithdrawalRecord(
                id = "w_${System.currentTimeMillis()}",
                amount = validAmount,
                method = method,
                destination = destination.ifBlank { "Primary Account" },
                timestampLabel = "Just now",
                statusLabel = "Instant Payout Sent"
            )

            state.copy(
                totalBalance = (state.totalBalance - validAmount).coerceAtLeast(0.0),
                withdrawalHistory = listOf(newRecord) + state.withdrawalHistory,
                isWithdrawSheetOpen = false,
                statusBannerMessage = String.format(
                    Locale.US,
                    "Withdrew \$%.2f via %s!",
                    validAmount,
                    method
                )
            )
        }
    }

    fun toggleProMembership(enablePro: Boolean) {
        _uiState.update { state ->
            state.copy(
                isProMember = enablePro,
                statusBannerMessage = if (enablePro) {
                    "Upgraded to Pro | Gold Tier! Mission rewards boosted up to \$15.50+."
                } else {
                    "Switched to Starter | Bronze Tier."
                }
            )
        }
    }

    fun resetDemoSnapshot() {
        _uiState.value = MissionUiState(
            statusBannerMessage = "Restored initial snapshot (\$75.95 • 16 Approved • 181/780 XP)."
        )
    }

    fun getRankings(state: MissionUiState): List<RankingEntry> {
        val entries = listOf(
            RankingEntry(
                rank = 1,
                initial = "A",
                name = "Alex Rivera",
                tierSubtitle = "Pro | Diamond Tier",
                level = 18,
                xp = 690,
                lifetimeEarned = 1480.50
            ),
            RankingEntry(
                rank = 2,
                initial = "S",
                name = "Sora Takahashi",
                tierSubtitle = "Pro | Gold Tier",
                level = 14,
                xp = 540,
                lifetimeEarned = 912.40
            ),
            RankingEntry(
                rank = 3,
                initial = "M",
                name = "Marcus Vance",
                tierSubtitle = "Pro | Gold Tier",
                level = 9,
                xp = 410,
                lifetimeEarned = 345.80
            ),
            RankingEntry(
                rank = 4,
                initial = state.avatarInitial,
                name = "${state.userName} (You)",
                tierSubtitle = state.tierSubtitle,
                level = state.level,
                xp = state.currentXp,
                lifetimeEarned = state.lifetimeEarnings,
                isCurrentUser = true
            ),
            RankingEntry(
                rank = 5,
                initial = "K",
                name = "Kira Lindqvist",
                tierSubtitle = "Starter | Bronze Tier",
                level = 4,
                xp = 140,
                lifetimeEarned = 64.20
            ),
            RankingEntry(
                rank = 6,
                initial = "D",
                name = "Devon Brooks",
                tierSubtitle = "Starter | Bronze Tier",
                level = 3,
                xp = 610,
                lifetimeEarned = 49.60
            )
        )
        return entries
    }
}
