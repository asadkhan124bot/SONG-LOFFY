import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:google_fonts/google_fonts.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  SystemChrome.setSystemUIOverlayStyle(
    const SystemUiOverlayStyle(
      statusBarColor: Colors.transparent,
      statusBarIconBrightness: Brightness.light,
      systemNavigationBarColor: Color(0xFF1A1A23),
      systemNavigationBarIconBrightness: Brightness.light,
    ),
  );
  runApp(const MissionPayApp());
}

class AppColors {
  static const Color background = Color(0xFF0F0F14);
  static const Color cardSurface = Color(0xFF1F1F2A);
  static const Color innerBox = Color(0xFF2C2C38);
  static const Color bottomNav = Color(0xFF1A1A23);
  static const Color amberTier = Color(0xFFFFB72B);
  static const Color rewardGreen = Color(0xFF22C55E);
  static const Color textGrey = Color(0xFF9496A4);
  static const Color white12 = Colors.white12;
}

class MissionItem {
  final String id;
  final String title;
  final String category;
  final double starterReward;
  final double proReward;
  final int xpReward;
  final IconData icon;
  final String instructions;
  bool isCompleted;

  MissionItem({
    required this.id,
    required this.title,
    required this.category,
    required this.starterReward,
    required this.proReward,
    required this.xpReward,
    required this.icon,
    required this.instructions,
    this.isCompleted = false,
  });

  String get rewardLabel =>
      '+\$${starterReward.toStringAsFixed(2)} • \$${proReward.toStringAsFixed(2)} with Pro';
}

class LeaderboardUser {
  final int rank;
  final String initial;
  final String name;
  final String tier;
  final int level;
  final int xp;
  final double earned;
  final bool isCurrentUser;

  const LeaderboardUser({
    required this.rank,
    required this.initial,
    required this.name,
    required this.tier,
    required this.level,
    required this.xp,
    required this.earned,
    this.isCurrentUser = false,
  });
}

class MissionPayApp extends StatelessWidget {
  const MissionPayApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'MissionPay',
      debugShowCheckedModeBanner: false,
      themeMode: ThemeMode.dark,
      darkTheme: ThemeData(
        useMaterial3: true,
        brightness: Brightness.dark,
        scaffoldBackgroundColor: AppColors.background,
        colorScheme: const ColorScheme.dark(
          primary: Colors.white,
          onPrimary: Colors.black,
          secondary: AppColors.amberTier,
          surface: AppColors.cardSurface,
        ),
        textTheme: GoogleFonts.interTextTheme(ThemeData.dark().textTheme),
      ),
      home: const MainShellScreen(),
    );
  }
}

class MainShellScreen extends StatefulWidget {
  const MainShellScreen({super.key});

  @override
  State<MainShellScreen> createState() => _MainShellScreenState();
}

class _MainShellScreenState extends State<MainShellScreen> {
  int _selectedIndex = 0;
  double _totalBalance = 75.95;
  double _lifetimeBalance = 75.95;
  int _approvedCount = 16;
  int _level = 4;
  int _currentXp = 181;
  final int _maxXp = 780;
  int _todayCompleted = 2;
  final int _todayTarget = 5;
  int _streakDays = 1;
  bool _isPro = false;

  late final List<MissionItem> _missions = [
    MissionItem(
      id: 'm1',
      title: 'Eraser on any surface',
      category: 'Desk & Stationery',
      starterReward: 2.40,
      proReward: 15.50,
      xpReward: 65,
      icon: Icons.auto_fix_normal_rounded,
      instructions:
          'Place a standard rubber or vinyl eraser on any textured surface (wood, matte desk, notebook) and capture a clear 5-second clip.',
    ),
    MissionItem(
      id: 'm2',
      title: 'Coffee mug under warm light',
      category: 'Indoor Lighting',
      starterReward: 3.10,
      proReward: 18.00,
      xpReward: 80,
      icon: Icons.coffee_rounded,
      instructions:
          'Record a ceramic mug from a 45-degree angle with natural or warm lamp lighting.',
    ),
    MissionItem(
      id: 'm3',
      title: 'Mechanical keyboard keycap',
      category: 'Tech & Hardware',
      starterReward: 2.85,
      proReward: 16.40,
      xpReward: 75,
      icon: Icons.keyboard_alt_outlined,
      instructions:
          'Focus closely on keyboard keycaps showing surface texture and legends clearly.',
    ),
    MissionItem(
      id: 'm4',
      title: 'Metallic coin on wooden desk',
      category: 'Everyday Objects',
      starterReward: 2.20,
      proReward: 14.00,
      xpReward: 55,
      icon: Icons.monetization_on_outlined,
      instructions:
          'Capture specular reflections on a coin resting flat on a wooden surface.',
    ),
    MissionItem(
      id: 'm5',
      title: 'Sneaker sole tread pattern',
      category: 'Apparel & Footwear',
      starterReward: 3.60,
      proReward: 21.00,
      xpReward: 95,
      icon: Icons.directions_run_rounded,
      instructions:
          'Show clean rubber outsole geometry under even indoor lighting.',
    ),
  ];

  double get _xpProgress => (_currentXp / _maxXp).clamp(0.0, 1.0);

  MissionItem get _nextMission =>
      _missions.firstWhere((m) => !m.isCompleted, orElse: () => _missions.first);

  void _completeMission(MissionItem mission) {
    final reward = _isPro ? mission.proReward : mission.starterReward;
    setState(() {
      mission.isCompleted = true;
      _totalBalance += reward;
      _lifetimeBalance += reward;
      _approvedCount += 1;
      if (_todayCompleted < _todayTarget) {
        _todayCompleted += 1;
      }
      _currentXp += mission.xpReward;
      if (_currentXp >= _maxXp) {
        _level += 1;
        _currentXp -= _maxXp;
      }
    });
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        backgroundColor: AppColors.cardSurface,
        behavior: SnackBarBehavior.floating,
        content: Text(
          'Mission approved! +\$${reward.toStringAsFixed(2)} & +${mission.xpReward} XP added.',
          style: GoogleFonts.inter(color: Colors.white),
        ),
      ),
    );
  }

  void _openWithdrawSheet() {
    final controller =
        TextEditingController(text: _totalBalance.toStringAsFixed(2));
    String selectedMethod = 'PayPal Instant';

    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: AppColors.cardSurface,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(28)),
      ),
      builder: (ctx) {
        return StatefulBuilder(
          builder: (ctx, setModalState) {
            return Padding(
              padding: EdgeInsets.only(
                left: 24,
                right: 24,
                top: 24,
                bottom: MediaQuery.of(ctx).viewInsets.bottom + 28,
              ),
              child: Column(
                mainAxisSize: MainAxisSize.min,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'Withdraw Balance',
                    style: GoogleFonts.poppins(
                      fontSize: 20,
                      fontWeight: FontWeight.w700,
                      color: Colors.white,
                    ),
                  ),
                  const SizedBox(height: 6),
                  Text(
                    'Available: \$${_totalBalance.toStringAsFixed(2)}',
                    style: GoogleFonts.inter(
                      fontSize: 13,
                      color: AppColors.textGrey,
                    ),
                  ),
                  const SizedBox(height: 18),
                  Wrap(
                    spacing: 10,
                    children: ['PayPal Instant', 'Bank Transfer', 'USDC Wallet']
                        .map((method) {
                      final selected = selectedMethod == method;
                      return ChoiceChip(
                        label: Text(method),
                        selected: selected,
                        onSelected: (_) =>
                            setModalState(() => selectedMethod = method),
                        selectedColor: Colors.white,
                        backgroundColor: AppColors.innerBox,
                        labelStyle: GoogleFonts.inter(
                          color: selected ? Colors.black : Colors.white,
                          fontWeight: FontWeight.w600,
                          fontSize: 12,
                        ),
                      );
                    }).toList(),
                  ),
                  const SizedBox(height: 16),
                  TextField(
                    controller: controller,
                    keyboardType:
                        const TextInputType.numberWithOptions(decimal: true),
                    style: GoogleFonts.poppins(
                      fontSize: 22,
                      fontWeight: FontWeight.bold,
                      color: Colors.white,
                    ),
                    decoration: InputDecoration(
                      prefixText: '\$ ',
                      filled: true,
                      fillColor: AppColors.innerBox,
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(16),
                        borderSide: BorderSide.none,
                      ),
                    ),
                  ),
                  const SizedBox(height: 20),
                  SizedBox(
                    width: double.infinity,
                    height: 52,
                    child: ElevatedButton(
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.white,
                        foregroundColor: Colors.black,
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(16),
                        ),
                      ),
                      onPressed: () {
                        final amount =
                            double.tryParse(controller.text.trim()) ?? 0.0;
                        if (amount <= 0 || amount > _totalBalance) return;
                        setState(() {
                          _totalBalance =
                              (_totalBalance - amount).clamp(0.0, 999999.0);
                        });
                        Navigator.pop(ctx);
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(
                            backgroundColor: AppColors.cardSurface,
                            behavior: SnackBarBehavior.floating,
                            content: Text(
                              'Withdrawal of \$${amount.toStringAsFixed(2)} via $selectedMethod initiated!',
                              style: GoogleFonts.inter(color: Colors.white),
                            ),
                          ),
                        );
                      },
                      child: Text(
                        'Confirm Withdrawal',
                        style: GoogleFonts.inter(
                          fontWeight: FontWeight.w700,
                          fontSize: 15,
                          color: Colors.black,
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            );
          },
        );
      },
    );
  }

  void _openMissionSheet(MissionItem mission) {
    showModalBottomSheet(
      context: context,
      backgroundColor: AppColors.cardSurface,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(28)),
      ),
      builder: (ctx) {
        return Padding(
          padding: const EdgeInsets.fromLTRB(24, 24, 24, 32),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: [
                  Container(
                    width: 48,
                    height: 48,
                    decoration: BoxDecoration(
                      color: AppColors.innerBox,
                      borderRadius: BorderRadius.circular(14),
                    ),
                    child: Icon(mission.icon, color: Colors.white, size: 24),
                  ),
                  const SizedBox(width: 14),
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          mission.title,
                          style: GoogleFonts.poppins(
                            fontSize: 18,
                            fontWeight: FontWeight.w700,
                            color: Colors.white,
                          ),
                        ),
                        const SizedBox(height: 2),
                        Text(
                          mission.rewardLabel,
                          style: GoogleFonts.inter(
                            fontSize: 13,
                            fontWeight: FontWeight.w600,
                            color: AppColors.rewardGreen,
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 18),
              Text(
                mission.instructions,
                style: GoogleFonts.inter(
                  fontSize: 14,
                  color: AppColors.textGrey,
                  height: 1.45,
                ),
              ),
              const SizedBox(height: 22),
              SizedBox(
                width: double.infinity,
                height: 52,
                child: ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.white,
                    foregroundColor: Colors.black,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(16),
                    ),
                  ),
                  onPressed: () {
                    Navigator.pop(ctx);
                    _completeMission(mission);
                  },
                  child: Text(
                    'Submit & Claim +\$${(_isPro ? mission.proReward : mission.starterReward).toStringAsFixed(2)}',
                    style: GoogleFonts.inter(
                      fontSize: 15,
                      fontWeight: FontWeight.w700,
                      color: Colors.black,
                    ),
                  ),
                ),
              ),
            ],
          ),
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      body: SafeArea(
        child: Center(
          child: ConstrainedBox(
            constraints: const BoxConstraints(maxWidth: 560),
            child: IndexedStack(
              index: _selectedIndex,
              children: [
                _buildHomeTab(),
                _buildMissionsTab(),
                _buildRankingTab(),
                _buildPlansTab(),
              ],
            ),
          ),
        ),
      ),
      bottomNavigationBar: Container(
        decoration: const BoxDecoration(
          color: AppColors.bottomNav,
          border: Border(
            top: BorderSide(color: Colors.white10, width: 1),
          ),
        ),
        child: SafeArea(
          top: false,
          child: SizedBox(
            height: 68,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                _buildNavItem(0, Icons.home_filled, Icons.home_outlined, 'Home'),
                _buildNavItem(1, Icons.bolt_rounded, Icons.bolt_outlined, 'Missions'),
                _buildNavItem(
                    2, Icons.emoji_events_rounded, Icons.emoji_events_outlined, 'Ranking'),
                _buildNavItem(
                    3, Icons.workspace_premium_rounded, Icons.workspace_premium_outlined, 'Plans'),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildNavItem(
      int index, IconData activeIcon, IconData inactiveIcon, String label) {
    final isSelected = _selectedIndex == index;
    return InkWell(
      borderRadius: BorderRadius.circular(14),
      onTap: () => setState(() => _selectedIndex = index),
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(
              isSelected ? activeIcon : inactiveIcon,
              color: isSelected ? Colors.white : AppColors.textGrey,
              size: 23,
            ),
            const SizedBox(height: 4),
            Text(
              label,
              style: GoogleFonts.inter(
                fontSize: 11.5,
                fontWeight: isSelected ? FontWeight.w600 : FontWeight.w500,
                color: isSelected ? Colors.white : AppColors.textGrey,
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildHomeTab() {
    final nextMission = _nextMission;
    return SingleChildScrollView(
      padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Top Header: Avatar "H", name "Guest", subtitle "Starter | Bronze Tier" in amber, notification icon right
          Row(
            children: [
              Container(
                width: 48,
                height: 48,
                decoration: BoxDecoration(
                  color: AppColors.cardSurface,
                  shape: BoxShape.circle,
                  border: Border.all(color: Colors.white12, width: 1.2),
                ),
                alignment: Alignment.center,
                child: Text(
                  'H',
                  style: GoogleFonts.poppins(
                    fontSize: 20,
                    fontWeight: FontWeight.w700,
                    color: Colors.white,
                  ),
                ),
              ),
              const SizedBox(width: 14),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      'Guest',
                      style: GoogleFonts.poppins(
                        fontSize: 18,
                        fontWeight: FontWeight.w700,
                        color: Colors.white,
                      ),
                    ),
                    const SizedBox(height: 2),
                    Text(
                      _isPro ? 'Pro | Gold Tier' : 'Starter | Bronze Tier',
                      style: GoogleFonts.inter(
                        fontSize: 13,
                        fontWeight: FontWeight.w600,
                        color: AppColors.amberTier,
                      ),
                    ),
                  ],
                ),
              ),
              Container(
                width: 44,
                height: 44,
                decoration: BoxDecoration(
                  color: AppColors.cardSurface,
                  borderRadius: BorderRadius.circular(14),
                  border: Border.all(color: Colors.white12),
                ),
                child: IconButton(
                  icon: const Icon(
                    Icons.notifications_none_rounded,
                    color: Colors.white,
                    size: 22,
                  ),
                  onPressed: () {
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(
                        backgroundColor: AppColors.cardSurface,
                        behavior: SnackBarBehavior.floating,
                        content: Text(
                          '16 missions approved • Next mission ready: ${nextMission.title}',
                          style: GoogleFonts.inter(color: Colors.white),
                        ),
                      ),
                    );
                  },
                ),
              ),
            ],
          ),
          const SizedBox(height: 24),

          // Middle Card 1: Background #1F1F2A, rounded 24
          Container(
            width: double.infinity,
            padding: const EdgeInsets.all(22),
            decoration: BoxDecoration(
              color: AppColors.cardSurface,
              borderRadius: BorderRadius.circular(24),
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  'TOTAL BALANCE',
                  style: GoogleFonts.inter(
                    fontSize: 11.5,
                    fontWeight: FontWeight.w600,
                    letterSpacing: 1.1,
                    color: AppColors.textGrey,
                  ),
                ),
                const SizedBox(height: 8),
                Text(
                  '\$${_totalBalance.toStringAsFixed(2)}',
                  style: GoogleFonts.poppins(
                    fontSize: 38,
                    fontWeight: FontWeight.w700,
                    color: Colors.white,
                    height: 1.15,
                  ),
                ),
                const SizedBox(height: 18),
                Row(
                  children: [
                    Expanded(
                      child: Container(
                        padding: const EdgeInsets.symmetric(
                            horizontal: 14, vertical: 12),
                        decoration: BoxDecoration(
                          color: AppColors.innerBox,
                          borderRadius: BorderRadius.circular(14),
                        ),
                        alignment: Alignment.center,
                        child: Text(
                          '$_approvedCount Approved',
                          style: GoogleFonts.inter(
                            fontSize: 13.5,
                            fontWeight: FontWeight.w600,
                            color: Colors.white,
                          ),
                        ),
                      ),
                    ),
                    const SizedBox(width: 12),
                    Expanded(
                      child: Container(
                        padding: const EdgeInsets.symmetric(
                            horizontal: 14, vertical: 12),
                        decoration: BoxDecoration(
                          color: AppColors.innerBox,
                          borderRadius: BorderRadius.circular(14),
                        ),
                        alignment: Alignment.center,
                        child: Text(
                          '\$${_lifetimeBalance.toStringAsFixed(2)} Lifetime',
                          style: GoogleFonts.inter(
                            fontSize: 13.5,
                            fontWeight: FontWeight.w600,
                            color: Colors.white,
                          ),
                        ),
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 18),
                SizedBox(
                  width: double.infinity,
                  height: 52,
                  child: ElevatedButton(
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.white,
                      foregroundColor: Colors.black,
                      elevation: 0,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(16),
                      ),
                    ),
                    onPressed: _openWithdrawSheet,
                    child: Text(
                      'Withdraw',
                      style: GoogleFonts.inter(
                        fontSize: 15.5,
                        fontWeight: FontWeight.w700,
                        color: Colors.black,
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),
          const SizedBox(height: 18),

          // Middle Card 2: Background #1F1F2A, Level 4 left, 181/780 XP right, progress bar 0.23 white, below Today: 2/5 missions & Streak: 1d 🔥
          Container(
            width: double.infinity,
            padding: const EdgeInsets.all(20),
            decoration: BoxDecoration(
              color: AppColors.cardSurface,
              borderRadius: BorderRadius.circular(24),
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(
                      'Level $_level',
                      style: GoogleFonts.poppins(
                        fontSize: 17,
                        fontWeight: FontWeight.w700,
                        color: Colors.white,
                      ),
                    ),
                    Text(
                      '$_currentXp/$_maxXp XP',
                      style: GoogleFonts.inter(
                        fontSize: 13.5,
                        fontWeight: FontWeight.w600,
                        color: AppColors.textGrey,
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 14),
                ClipRRect(
                  borderRadius: BorderRadius.circular(8),
                  child: LinearProgressIndicator(
                    value: _xpProgress,
                    minHeight: 8,
                    backgroundColor: AppColors.innerBox,
                    valueColor:
                        const AlwaysStoppedAnimation<Color>(Colors.white),
                  ),
                ),
                const SizedBox(height: 14),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(
                      'Today: $_todayCompleted/$_todayTarget missions',
                      style: GoogleFonts.inter(
                        fontSize: 13,
                        fontWeight: FontWeight.w500,
                        color: AppColors.textGrey,
                      ),
                    ),
                    Text(
                      'Streak: ${_streakDays}d 🔥',
                      style: GoogleFonts.inter(
                        fontSize: 13,
                        fontWeight: FontWeight.w600,
                        color: Colors.white,
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),
          const SizedBox(height: 24),

          // Bottom Card: Title "Next Mission", card with border white12, icon eraser, title "Eraser on any surface", green text "+$2.40 • $15.50 with Pro", arrow right
          Text(
            'Next Mission',
            style: GoogleFonts.poppins(
              fontSize: 17,
              fontWeight: FontWeight.w600,
              color: Colors.white,
            ),
          ),
          const SizedBox(height: 12),
          InkWell(
            borderRadius: BorderRadius.circular(20),
            onTap: () => _openMissionSheet(nextMission),
            child: Container(
              width: double.infinity,
              padding: const EdgeInsets.symmetric(horizontal: 18, vertical: 18),
              decoration: BoxDecoration(
                color: AppColors.cardSurface,
                borderRadius: BorderRadius.circular(20),
                border: Border.all(color: AppColors.white12, width: 1.2),
              ),
              child: Row(
                children: [
                  Container(
                    width: 48,
                    height: 48,
                    decoration: BoxDecoration(
                      color: AppColors.innerBox,
                      borderRadius: BorderRadius.circular(14),
                    ),
                    child: Icon(
                      nextMission.icon,
                      color: Colors.white,
                      size: 24,
                    ),
                  ),
                  const SizedBox(width: 14),
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          nextMission.title,
                          style: GoogleFonts.poppins(
                            fontSize: 15.5,
                            fontWeight: FontWeight.w600,
                            color: Colors.white,
                          ),
                        ),
                        const SizedBox(height: 4),
                        Text(
                          nextMission.rewardLabel,
                          style: GoogleFonts.inter(
                            fontSize: 13,
                            fontWeight: FontWeight.w600,
                            color: AppColors.rewardGreen,
                          ),
                        ),
                      ],
                    ),
                  ),
                  const Icon(
                    Icons.arrow_forward_ios_rounded,
                    color: Colors.white70,
                    size: 18,
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildMissionsTab() {
    return ListView.separated(
      padding: const EdgeInsets.all(20),
      itemCount: _missions.length,
      separatorBuilder: (_, __) => const SizedBox(height: 12),
      itemBuilder: (context, index) {
        final m = _missions[index];
        return InkWell(
          borderRadius: BorderRadius.circular(20),
          onTap: () => _openMissionSheet(m),
          child: Container(
            padding: const EdgeInsets.all(18),
            decoration: BoxDecoration(
              color: AppColors.cardSurface,
              borderRadius: BorderRadius.circular(20),
              border: Border.all(color: AppColors.white12),
            ),
            child: Row(
              children: [
                Container(
                  width: 46,
                  height: 46,
                  decoration: BoxDecoration(
                    color: AppColors.innerBox,
                    borderRadius: BorderRadius.circular(14),
                  ),
                  child: Icon(m.icon, color: Colors.white, size: 22),
                ),
                const SizedBox(width: 14),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        m.title,
                        style: GoogleFonts.poppins(
                          fontSize: 15,
                          fontWeight: FontWeight.w600,
                          color: Colors.white,
                        ),
                      ),
                      const SizedBox(height: 4),
                      Text(
                        m.rewardLabel,
                        style: GoogleFonts.inter(
                          fontSize: 12.5,
                          fontWeight: FontWeight.w600,
                          color: AppColors.rewardGreen,
                        ),
                      ),
                    ],
                  ),
                ),
                const Icon(Icons.arrow_forward_ios_rounded,
                    color: Colors.white70, size: 16),
              ],
            ),
          ),
        );
      },
    );
  }

  Widget _buildRankingTab() {
    final leaders = [
      const LeaderboardUser(
        rank: 1,
        initial: 'A',
        name: 'Alex Rivera',
        tier: 'Pro | Diamond Tier',
        level: 19,
        xp: 14820,
        earned: 1420.50,
      ),
      const LeaderboardUser(
        rank: 2,
        initial: 'S',
        name: 'Sora Takahashi',
        tier: 'Pro | Gold Tier',
        level: 15,
        xp: 11290,
        earned: 985.20,
      ),
      LeaderboardUser(
        rank: 3,
        initial: 'H',
        name: 'Guest (You)',
        tier: _isPro ? 'Pro | Gold Tier' : 'Starter | Bronze Tier',
        level: _level,
        xp: _currentXp,
        earned: _lifetimeBalance,
        isCurrentUser: true,
      ),
    ];

    return ListView.separated(
      padding: const EdgeInsets.all(20),
      itemCount: leaders.length,
      separatorBuilder: (_, __) => const SizedBox(height: 12),
      itemBuilder: (context, index) {
        final user = leaders[index];
        return Container(
          padding: const EdgeInsets.all(18),
          decoration: BoxDecoration(
            color: AppColors.cardSurface,
            borderRadius: BorderRadius.circular(20),
            border: Border.all(
              color: user.isCurrentUser ? AppColors.amberTier : AppColors.white12,
            ),
          ),
          child: Row(
            children: [
              Text(
                '#${user.rank}',
                style: GoogleFonts.poppins(
                  fontSize: 16,
                  fontWeight: FontWeight.w700,
                  color: AppColors.amberTier,
                ),
              ),
              const SizedBox(width: 14),
              CircleAvatar(
                backgroundColor: AppColors.innerBox,
                child: Text(
                  user.initial,
                  style: GoogleFonts.poppins(
                    color: Colors.white,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
              const SizedBox(width: 12),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      user.name,
                      style: GoogleFonts.poppins(
                        fontSize: 15,
                        fontWeight: FontWeight.w600,
                        color: Colors.white,
                      ),
                    ),
                    Text(
                      'Lv. ${user.level} • ${user.tier}',
                      style: GoogleFonts.inter(
                        fontSize: 12,
                        color: AppColors.textGrey,
                      ),
                    ),
                  ],
                ),
              ),
              Text(
                '\$${user.earned.toStringAsFixed(2)}',
                style: GoogleFonts.poppins(
                  fontSize: 15,
                  fontWeight: FontWeight.w700,
                  color: AppColors.rewardGreen,
                ),
              ),
            ],
          ),
        );
      },
    );
  }

  Widget _buildPlansTab() {
    return Padding(
      padding: const EdgeInsets.all(20),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            width: double.infinity,
            padding: const EdgeInsets.all(22),
            decoration: BoxDecoration(
              color: AppColors.cardSurface,
              borderRadius: BorderRadius.circular(24),
              border: Border.all(color: AppColors.amberTier, width: 1.4),
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  'MissionPay Pro',
                  style: GoogleFonts.poppins(
                    fontSize: 22,
                    fontWeight: FontWeight.w700,
                    color: Colors.white,
                  ),
                ),
                const SizedBox(height: 8),
                Text(
                  'Unlock \$15.50+ per mission (6.4x multiplier), instant 0-fee withdrawals, and Gold Tier status.',
                  style: GoogleFonts.inter(
                    fontSize: 13.5,
                    color: AppColors.textGrey,
                    height: 1.45,
                  ),
                ),
                const SizedBox(height: 20),
                SizedBox(
                  width: double.infinity,
                  height: 50,
                  child: ElevatedButton(
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.white,
                      foregroundColor: Colors.black,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(16),
                      ),
                    ),
                    onPressed: () => setState(() => _isPro = !_isPro),
                    child: Text(
                      _isPro ? 'Active: Pro Tier (Switch to Starter)' : 'Upgrade to Pro',
                      style: GoogleFonts.inter(
                        fontWeight: FontWeight.w700,
                        color: Colors.black,
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
