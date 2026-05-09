package com.mhj.anime.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mhj.anime.entity.SysRole;
import com.mhj.anime.entity.SysUser;
import com.mhj.anime.entity.SysUserRole;
import com.mhj.anime.service.SysRoleService;
import com.mhj.anime.service.SysUserRoleService;
import com.mhj.anime.service.SysUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SysRoleService sysRoleService;
    private final SysUserService sysUserService;
    private final SysUserRoleService sysUserRoleService;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    public DataInitializer(SysRoleService sysRoleService,
                           SysUserService sysUserService,
                           SysUserRoleService sysUserRoleService,
                           PasswordEncoder passwordEncoder,
                           JdbcTemplate jdbcTemplate) {
        this.sysRoleService = sysRoleService;
        this.sysUserService = sysUserService;
        this.sysUserRoleService = sysUserRoleService;
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        ensureSchema();
        SysRole adminRole = ensureRole("ADMIN", "管理员");
        SysRole userRole = ensureRole("USER", "普通用户");
        ensureUser("admin", "admin123", "系统管理员", "admin@example.com", adminRole);
        ensureUser("demo", "demo123", "演示用户", "demo@example.com", userRole);
        seedCategories();
        seedTags();
        seedAnime();
        seedNews();
    }

    private void ensureSchema() {
        addColumnIfMissing("anime_info", "type", "ALTER TABLE anime_info ADD COLUMN type VARCHAR(30) NULL COMMENT '作品类型' AFTER region");
        addColumnIfMissing("anime_info", "tag_names", "ALTER TABLE anime_info ADD COLUMN tag_names VARCHAR(300) NULL COMMENT '标签名称' AFTER type");
        addColumnIfMissing("anime_info", "source_name", "ALTER TABLE anime_info ADD COLUMN source_name VARCHAR(100) NULL COMMENT '正版来源平台' AFTER description");
        addColumnIfMissing("anime_info", "watch_url", "ALTER TABLE anime_info ADD COLUMN watch_url VARCHAR(500) NULL COMMENT '正版观看入口' AFTER source_name");
        addColumnIfMissing("anime_info", "trailer_url", "ALTER TABLE anime_info ADD COLUMN trailer_url VARCHAR(500) NULL COMMENT '官方预告入口' AFTER watch_url");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS anime_tag (" +
                "id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50) NOT NULL UNIQUE, " +
                "description VARCHAR(255) NULL, " +
                "sort_order INT NOT NULL DEFAULT 0, " +
                "create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");
    }

    private void addColumnIfMissing(String tableName, String columnName, String sql) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? AND COLUMN_NAME = ?",
                Integer.class,
                tableName,
                columnName);
        if (count == null || count == 0) {
            jdbcTemplate.execute(sql);
        }
    }

    private SysRole ensureRole(String roleCode, String roleName) {
        SysRole role = sysRoleService.getOne(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleCode, roleCode)
                .last("LIMIT 1"));
        if (role == null) {
            role = new SysRole();
            role.setRoleCode(roleCode);
            role.setRoleName(roleName);
            sysRoleService.save(role);
        } else if (!roleName.equals(role.getRoleName())) {
            role.setRoleName(roleName);
            sysRoleService.updateById(role);
        }
        return role;
    }

    private void ensureUser(String username, String rawPassword, String nickname, String email, SysRole role) {
        SysUser user = sysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username)
                .last("LIMIT 1"));
        if (user == null) {
            user = new SysUser();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setNickname(nickname);
            user.setEmail(email);
            user.setStatus(1);
            sysUserService.save(user);
        } else {
            boolean changed = false;
            if (user.getPassword() == null || !user.getPassword().startsWith("$2")) {
                user.setPassword(passwordEncoder.encode(rawPassword));
                changed = true;
            }
            if (user.getStatus() == null) {
                user.setStatus(1);
                changed = true;
            }
            if (changed) {
                sysUserService.updateById(user);
            }
        }

        boolean relationExists = sysUserRoleService.count(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, user.getId())
                .eq(SysUserRole::getRoleId, role.getId())) > 0;
        if (!relationExists) {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(role.getId());
            sysUserRoleService.save(userRole);
        }
    }

    private void seedCategories() {
        seedCategory("动作", "战斗、热血、冒险等高强度作品", 1);
        seedCategory("奇幻", "魔法、异世界、架空世界观作品", 2);
        seedCategory("科幻", "未来科技、人工智能、赛博朋克等题材", 3);
        seedCategory("悬疑", "推理、时间循环、反转叙事作品", 4);
        seedCategory("恋爱", "青春恋爱、校园情感作品", 5);
        seedCategory("日常", "生活、家庭、轻喜剧作品", 6);
        seedCategory("运动", "竞技、团队成长、比赛题材作品", 7);
        seedCategory("音乐", "乐队、偶像、舞台表演作品", 8);
        seedCategory("治愈", "温暖、成长、情绪表达作品", 9);
        seedCategory("冒险", "旅行、探索、成长冒险作品", 10);
        seedCategory("剧场版", "动画电影与院线作品", 11);
    }

    private void seedCategory(String name, String description, int sortOrder) {
        upsertSimple("anime_category", name, description, sortOrder);
    }

    private void seedTags() {
        String[][] tags = {
                {"战斗", "战斗演出和能力对抗突出"}, {"热血", "强调成长、信念和对抗"}, {"校园", "以校园生活或青春关系为核心"},
                {"漫画改", "根据漫画作品改编"}, {"轻小说改", "根据轻小说作品改编"}, {"原创", "动画原创企划"},
                {"异世界", "异世界或转生设定"}, {"赛博朋克", "未来都市与身体改造题材"}, {"推理", "悬疑推理与谜题驱动"},
                {"家庭", "家庭关系和日常互动突出"}, {"成长", "角色成长线明确"}, {"催泪", "情绪感染力强"},
                {"美食", "饮食、烹饪和生活体验"}, {"音乐", "音乐表演和乐队活动"}, {"运动", "体育竞技题材"},
                {"电影", "剧场版动画"}, {"人工智能", "AI、未来科技相关"}, {"时间循环", "时间轮回或循环结构"}
        };
        for (int i = 0; i < tags.length; i++) {
            upsertSimple("anime_tag", tags[i][0], tags[i][1], i + 1);
        }
    }

    private void upsertSimple(String tableName, String name, String description, int sortOrder) {
        List<Long> ids = jdbcTemplate.query("SELECT id FROM " + tableName + " WHERE name = ? LIMIT 1",
                (rs, rowNum) -> rs.getLong("id"),
                name);
        if (ids.isEmpty()) {
            jdbcTemplate.update("INSERT INTO " + tableName + " (name, description, sort_order) VALUES (?, ?, ?)",
                    name, description, sortOrder);
        } else {
            jdbcTemplate.update("UPDATE " + tableName + " SET description = ?, sort_order = ? WHERE id = ?",
                    description, sortOrder, ids.get(0));
        }
    }

    private void seedAnime() {
        AnimeSeed[] seeds = {
                new AnimeSeed("葬送的芙莉莲", "Frieren: Beyond Journey's End", "https://myanimelist.net/images/anime/1015/138006l.jpg", "奇幻", "日本", "TV", "奇幻,冒险,成长,催泪", 2023, "已完结", 28, "9.3", "勇者一行击败魔王后，长寿精灵芙莉莲重新踏上旅途，在漫长时间里重新理解人类、离别与回忆。", "Crunchyroll", "https://www.crunchyroll.com/search?q=Frieren", "https://www.youtube.com/results?search_query=Frieren+Beyond+Journey%27s+End+official+trailer", 1432503),
                new AnimeSeed("咒术回战 第二季", "Jujutsu Kaisen Season 2", "https://myanimelist.net/images/anime/1792/138022l.jpg", "动作", "日本", "TV", "战斗,热血,漫画改", 2023, "已完结", 23, "8.7", "第二季覆盖怀玉・玉折与涩谷事变篇章，动作演出和角色冲突都具有很强的视觉冲击力。", "Crunchyroll", "https://www.crunchyroll.com/search?q=Jujutsu%20Kaisen", "https://www.youtube.com/results?search_query=Jujutsu+Kaisen+Season+2+official+trailer", 1394119),
                new AnimeSeed("电锯人", "Chainsaw Man", "https://myanimelist.net/images/anime/1806/126216l.jpg", "动作", "日本", "TV", "战斗,漫画改,成长", 2022, "已完结", 12, "8.4", "少年电次与电锯恶魔波奇塔合为一体，成为恶魔猎人后卷入危险又荒诞的战斗。", "Crunchyroll", "https://www.crunchyroll.com/search?q=Chainsaw%20Man", "https://www.youtube.com/results?search_query=Chainsaw+Man+official+trailer", 1962368),
                new AnimeSeed("SPY x FAMILY 间谍过家家", "Spy x Family", "https://myanimelist.net/images/anime/1441/122795l.jpg", "日常", "日本", "TV", "家庭,漫画改,喜剧", 2022, "已完结", 12, "8.4", "间谍、杀手和会读心的孩子组成临时家庭，在任务和日常之间制造温暖笑点。", "Crunchyroll", "https://www.crunchyroll.com/search?q=Spy%20x%20Family", "https://www.youtube.com/results?search_query=SPY+x+FAMILY+official+trailer", 1889578),
                new AnimeSeed("赛博朋克：边缘行者", "Cyberpunk: Edgerunners", "https://myanimelist.net/images/anime/1818/126435l.jpg", "科幻", "日本", "ONA", "赛博朋克,原创,催泪", 2022, "已完结", 10, "8.6", "少年大卫在夜之城成为边缘行者，经历速度、欲望与命运交织的故事。", "Netflix", "https://www.netflix.com/search?q=Cyberpunk%20Edgerunners", "https://www.youtube.com/results?search_query=Cyberpunk+Edgerunners+official+trailer", 821357),
                new AnimeSeed("鬼灭之刃", "Demon Slayer: Kimetsu no Yaiba", "https://myanimelist.net/images/anime/1286/99889l.jpg", "动作", "日本", "TV", "战斗,热血,漫画改,家庭", 2019, "已完结", 26, "8.4", "灶门炭治郎为让妹妹祢豆子恢复成人，踏上斩鬼与成长的旅程。", "Crunchyroll", "https://www.crunchyroll.com/search?q=Demon%20Slayer", "https://www.youtube.com/results?search_query=Demon+Slayer+official+trailer", 1769000),
                new AnimeSeed("进击的巨人 最终季", "Attack on Titan: Final Season", "https://myanimelist.net/images/anime/1000/110531l.jpg", "动作", "日本", "TV", "战斗,漫画改,悬疑", 2021, "已完结", 16, "8.8", "围绕巨人与人类世界真相展开最终阶段叙事，战争、信念与自由成为核心冲突。", "Crunchyroll", "https://www.crunchyroll.com/search?q=Attack%20on%20Titan", "https://www.youtube.com/results?search_query=Attack+on+Titan+Final+Season+official+trailer", 2213000),
                new AnimeSeed("冰海战记 第二季", "Vinland Saga Season 2", "https://myanimelist.net/images/anime/1170/124312l.jpg", "冒险", "日本", "TV", "成长,漫画改,历史", 2023, "已完结", 24, "8.8", "托尔芬在新的生活中面对过去的暴力与创伤，寻找真正的自由和生存意义。", "Crunchyroll", "https://www.crunchyroll.com/search?q=Vinland%20Saga", "https://www.youtube.com/results?search_query=Vinland+Saga+Season+2+official+trailer", 738000),
                new AnimeSeed("灵能百分百 III", "Mob Psycho 100 III", "https://myanimelist.net/images/anime/1228/125011l.jpg", "动作", "日本", "TV", "战斗,成长,漫画改", 2022, "已完结", 12, "8.7", "拥有强大超能力的影山茂夫继续面对青春、人际关系和自我认同。", "Crunchyroll", "https://www.crunchyroll.com/search?q=Mob%20Psycho%20100", "https://www.youtube.com/results?search_query=Mob+Psycho+100+III+official+trailer", 690000),
                new AnimeSeed("孤独摇滚！", "Bocchi the Rock!", "https://myanimelist.net/images/anime/1448/127956l.jpg", "音乐", "日本", "TV", "音乐,校园,成长,漫画改", 2022, "已完结", 12, "8.7", "社恐少女后藤一里加入乐队，在舞台、友情和音乐中慢慢打开自己。", "Crunchyroll", "https://www.crunchyroll.com/search?q=Bocchi%20the%20Rock", "https://www.youtube.com/results?search_query=Bocchi+the+Rock+official+trailer", 810000),
                new AnimeSeed("我推的孩子", "[Oshi No Ko]", "https://myanimelist.net/images/anime/1812/134736l.jpg", "悬疑", "日本", "TV", "漫画改,推理,成长", 2023, "已完结", 11, "8.5", "以演艺圈、偶像产业和复仇线索为核心，融合悬疑和行业观察。", "Netflix", "https://www.netflix.com/search?q=Oshi%20No%20Ko", "https://www.youtube.com/results?search_query=Oshi+No+Ko+official+trailer", 1050000),
                new AnimeSeed("我独自升级", "Solo Leveling", "https://myanimelist.net/images/anime/1801/142390l.jpg", "动作", "韩国", "TV", "战斗,热血,成长", 2024, "已完结", 12, "8.2", "低阶猎人成振宇获得独特升级能力，逐步挑战地下城与强敌。", "Crunchyroll", "https://www.crunchyroll.com/search?q=Solo%20Leveling", "https://www.youtube.com/results?search_query=Solo+Leveling+official+trailer", 980000),
                new AnimeSeed("迷宫饭", "Delicious in Dungeon", "https://myanimelist.net/images/anime/1711/142478l.jpg", "奇幻", "日本", "TV", "奇幻,美食,冒险,漫画改", 2024, "已完结", 24, "8.6", "莱欧斯一行人在迷宫中一边探索一边烹饪魔物，兼具冒险、喜剧与世界观设定。", "Netflix", "https://www.netflix.com/search?q=Delicious%20in%20Dungeon", "https://www.youtube.com/results?search_query=Delicious+in+Dungeon+official+trailer", 720000),
                new AnimeSeed("药屋少女的呢喃", "The Apothecary Diaries", "https://myanimelist.net/images/anime/1708/138033l.jpg", "悬疑", "日本", "TV", "推理,成长,轻小说改", 2023, "已完结", 24, "8.9", "猫猫凭借药学知识在后宫中破解事件，兼具推理、人物成长和古风氛围。", "Crunchyroll", "https://www.crunchyroll.com/search?q=The%20Apothecary%20Diaries", "https://www.youtube.com/results?search_query=The+Apothecary+Diaries+official+trailer", 860000),
                new AnimeSeed("莉可丽丝", "Lycoris Recoil", "https://myanimelist.net/images/anime/1261/127311l.jpg", "动作", "日本", "TV", "原创,战斗,日常", 2022, "已完结", 13, "8.1", "隐藏组织少女特工在咖啡店日常与危险任务之间展现鲜明个性。", "Crunchyroll", "https://www.crunchyroll.com/search?q=Lycoris%20Recoil", "https://www.youtube.com/results?search_query=Lycoris+Recoil+official+trailer", 620000),
                new AnimeSeed("奇巧计程车", "Odd Taxi", "https://myanimelist.net/images/anime/1981/113348l.jpg", "悬疑", "日本", "TV", "原创,推理,悬疑", 2021, "已完结", 13, "8.6", "出租车司机小户川与乘客们的对话交织成一桩失踪案件的复杂真相。", "Crunchyroll", "https://www.crunchyroll.com/search?q=Odd%20Taxi", "https://www.youtube.com/results?search_query=Odd+Taxi+official+trailer", 430000),
                new AnimeSeed("国王排名", "Ranking of Kings", "https://myanimelist.net/images/anime/1347/117616l.jpg", "奇幻", "日本", "TV", "奇幻,成长,冒险", 2021, "已完结", 23, "8.5", "听障王子波吉在质疑与困境中寻找勇气，童话画风下承载复杂人物关系。", "Crunchyroll", "https://www.crunchyroll.com/search?q=Ranking%20of%20Kings", "https://www.youtube.com/results?search_query=Ranking+of+Kings+official+trailer", 760000),
                new AnimeSeed("紫罗兰永恒花园", "Violet Evergarden", "https://myanimelist.net/images/anime/1795/95088l.jpg", "治愈", "日本", "TV", "催泪,成长,轻小说改", 2018, "已完结", 13, "8.7", "曾经的少女兵器成为自动手记人偶，在书信中理解感情与爱的含义。", "Netflix", "https://www.netflix.com/search?q=Violet%20Evergarden", "https://www.youtube.com/results?search_query=Violet+Evergarden+official+trailer", 1020000),
                new AnimeSeed("来自深渊", "Made in Abyss", "https://myanimelist.net/images/anime/6/86733l.jpg", "冒险", "日本", "TV", "冒险,奇幻,漫画改", 2017, "已完结", 13, "8.6", "少女莉可与机器人雷格深入巨大深渊，探索美丽而危险的未知世界。", "HIDIVE", "https://www.hidive.com/search?q=Made%20in%20Abyss", "https://www.youtube.com/results?search_query=Made+in+Abyss+official+trailer", 690000),
                new AnimeSeed("辉夜大小姐想让我告白", "Kaguya-sama: Love is War", "https://myanimelist.net/images/anime/1295/106551l.jpg", "恋爱", "日本", "TV", "校园,恋爱,漫画改,喜剧", 2019, "已完结", 12, "8.4", "学生会长与副会长互有好感，却用智斗方式试图让对方先告白。", "Crunchyroll", "https://www.crunchyroll.com/search?q=Kaguya-sama", "https://www.youtube.com/results?search_query=Kaguya-sama+Love+is+War+official+trailer", 880000),
                new AnimeSeed("蓝色监狱", "Blue Lock", "https://myanimelist.net/images/anime/1258/126929l.jpg", "运动", "日本", "TV", "运动,热血,漫画改", 2022, "已完结", 24, "8.1", "日本足球选拔计划蓝色监狱聚集前锋，以极端竞争培养世界级射手。", "Crunchyroll", "https://www.crunchyroll.com/search?q=Blue%20Lock", "https://www.youtube.com/results?search_query=Blue+Lock+official+trailer", 940000),
                new AnimeSeed("无职转生", "Mushoku Tensei: Jobless Reincarnation", "https://myanimelist.net/images/anime/1530/117776l.jpg", "奇幻", "日本", "TV", "异世界,轻小说改,成长", 2021, "已完结", 11, "8.3", "重新降生到异世界的鲁迪乌斯以新的身份学习魔法、面对关系与成长。", "Crunchyroll", "https://www.crunchyroll.com/search?q=Mushoku%20Tensei", "https://www.youtube.com/results?search_query=Mushoku+Tensei+official+trailer", 840000),
                new AnimeSeed("堀与宫村", "Horimiya", "https://myanimelist.net/images/anime/1695/111486l.jpg", "恋爱", "日本", "TV", "校园,恋爱,漫画改", 2021, "已完结", 13, "8.2", "看似不同的堀与宫村在校外发现彼此真实一面，展开轻快青春恋爱故事。", "Crunchyroll", "https://www.crunchyroll.com/search?q=Horimiya", "https://www.youtube.com/results?search_query=Horimiya+official+trailer", 710000),
                new AnimeSeed("Vivy -Fluorite Eye's Song-", "Vivy -Fluorite Eye's Song-", "https://myanimelist.net/images/anime/1551/128960l.jpg", "科幻", "日本", "TV", "原创,人工智能,音乐", 2021, "已完结", 13, "8.4", "AI歌姬薇薇为了改变人类与人工智能战争的未来，展开跨越百年的使命。", "Crunchyroll", "https://www.crunchyroll.com/search?q=Vivy", "https://www.youtube.com/results?search_query=Vivy+Fluorite+Eye%27s+Song+official+trailer", 520000),
                new AnimeSeed("86 -不存在的战区-", "86 Eighty-Six", "https://myanimelist.net/images/anime/1987/117507l.jpg", "科幻", "日本", "TV", "轻小说改,战斗,成长", 2021, "已完结", 11, "8.4", "无人机战争背后隐藏着被剥夺身份的少年少女，作品兼具战争叙事与情感冲击。", "Crunchyroll", "https://www.crunchyroll.com/search?q=86%20Eighty-Six", "https://www.youtube.com/results?search_query=86+Eighty-Six+official+trailer", 610000),
                new AnimeSeed("铃芽之旅", "Suzume", "https://myanimelist.net/images/anime/1598/128450l.jpg", "剧场版", "日本", "剧场版", "电影,奇幻,成长", 2022, "已完结", 1, "8.2", "少女铃芽与神秘青年踏上关门之旅，在灾厄与记忆中面对失去和成长。", "Crunchyroll", "https://www.crunchyroll.com/search?q=Suzume", "https://www.youtube.com/results?search_query=Suzume+official+trailer", 790000),
                new AnimeSeed("天气之子", "Weathering with You", "https://myanimelist.net/images/anime/1880/101146l.jpg", "剧场版", "日本", "剧场版", "电影,恋爱,奇幻", 2019, "已完结", 1, "8.3", "离家少年帆高遇见拥有改变天气能力的少女阳菜，展开关于选择与世界的故事。", "Netflix", "https://www.netflix.com/search?q=Weathering%20with%20You", "https://www.youtube.com/results?search_query=Weathering+with+You+official+trailer", 960000),
                new AnimeSeed("声之形", "A Silent Voice", "https://myanimelist.net/images/anime/1122/96435l.jpg", "剧场版", "日本", "剧场版", "电影,校园,催泪,成长", 2016, "已完结", 1, "8.9", "曾经伤害听障少女的少年长大后试图弥补过错，作品关注校园伤害、救赎与沟通。", "Netflix", "https://www.netflix.com/search?q=A%20Silent%20Voice", "https://www.youtube.com/results?search_query=A+Silent+Voice+official+trailer", 1120000),
                new AnimeSeed("更衣人偶坠入爱河", "My Dress-Up Darling", "https://myanimelist.net/images/anime/1179/119897l.jpg", "恋爱", "日本", "TV", "校园,恋爱,漫画改", 2022, "已完结", 12, "8.1", "热爱雏人偶制作的五条与喜欢Cosplay的喜多川相遇，展开清爽青春故事。", "Crunchyroll", "https://www.crunchyroll.com/search?q=My%20Dress-Up%20Darling", "https://www.youtube.com/results?search_query=My+Dress-Up+Darling+official+trailer", 870000),
                new AnimeSeed("夏日重现", "Summer Time Rendering", "https://myanimelist.net/images/anime/1120/120796l.jpg", "悬疑", "日本", "TV", "悬疑,时间循环,漫画改", 2022, "已完结", 25, "8.5", "慎平回到故乡参加青梅竹马葬礼，却被卷入影子、死亡和时间循环构成的谜团。", "Disney+", "https://www.disneyplus.com/search/Summer%20Time%20Rendering", "https://www.youtube.com/results?search_query=Summer+Time+Rendering+official+trailer", 570000)
        };
        for (AnimeSeed seed : seeds) {
            upsertAnime(seed);
        }
    }

    private void upsertAnime(AnimeSeed seed) {
        Long categoryId = jdbcTemplate.queryForObject(
                "SELECT id FROM anime_category WHERE name = ? LIMIT 1",
                Long.class,
                seed.categoryName);
        List<Long> ids = jdbcTemplate.query("SELECT id FROM anime_info WHERE title = ? LIMIT 1",
                (rs, rowNum) -> rs.getLong("id"),
                seed.title);
        if (ids.isEmpty()) {
            jdbcTemplate.update(
                    "INSERT INTO anime_info (title, original_title, cover_image, category_id, category_name, region, type, tag_names, release_year, status, episodes, score, description, source_name, watch_url, trailer_url, view_count) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    seed.title, seed.originalTitle, seed.coverImage, categoryId, seed.categoryName, seed.region, seed.type, seed.tagNames,
                    seed.releaseYear, seed.status, seed.episodes, seed.score, seed.description, seed.sourceName, seed.watchUrl, seed.trailerUrl, seed.viewCount);
        } else {
            jdbcTemplate.update(
                    "UPDATE anime_info SET original_title = ?, cover_image = ?, category_id = ?, category_name = ?, region = ?, type = ?, tag_names = ?, release_year = ?, status = ?, episodes = ?, score = ?, description = ?, source_name = ?, watch_url = ?, trailer_url = ?, view_count = GREATEST(view_count, ?) WHERE id = ?",
                    seed.originalTitle, seed.coverImage, categoryId, seed.categoryName, seed.region, seed.type, seed.tagNames,
                    seed.releaseYear, seed.status, seed.episodes, seed.score, seed.description, seed.sourceName, seed.watchUrl, seed.trailerUrl, seed.viewCount, ids.get(0));
        }
    }

    private void seedNews() {
        jdbcTemplate.update(
                "DELETE FROM news_info WHERE title IN (?, ?) OR summary LIKE ? OR content LIKE ?",
                "在线动漫信息平台系统启动开发",
                "在线动漫信息平台完成新版首页与分类升级",
                "%数据库设计%",
                "%列表接口%");
        upsertNews(
                "春季热门推荐：奇幻与冒险题材持续升温",
                "https://myanimelist.net/images/anime/1015/138006l.jpg",
                "《葬送的芙莉莲》《迷宫饭》等作品带动奇幻题材热度，细腻叙事与完整世界观成为用户关注重点。",
                "近年奇幻类动画不再只强调战斗场面，也更重视旅途、成长与人物关系。《葬送的芙莉莲》以长寿精灵的视角重新理解离别与回忆，《迷宫饭》则把地下城冒险和美食设定结合，形成轻松但扎实的世界观。平台将此类作品归入奇幻、冒险、成长等标签，方便用户按兴趣检索。",
                426);
        upsertNews(
                "近十年高分动画电影盘点：从声之形到铃芽之旅",
                "https://myanimelist.net/images/anime/1122/96435l.jpg",
                "剧场版动画在画面表现、情感表达和社会议题上持续突破，适合作为动漫库的重要分类入口。",
                "近十年动画电影中，《声之形》关注校园伤害、沟通与救赎，《天气之子》和《铃芽之旅》则用奇幻设定承载青春、灾害与选择等主题。平台将剧场版作品独立作为作品类型，用户可以通过“剧场版”“电影”“成长”“催泪”等标签快速定位。",
                389);
        upsertNews(
                "标签检索指南：用题材快速找到喜欢的动漫",
                "https://myanimelist.net/images/anime/1711/142478l.jpg",
                "平台支持主分类、热门标签、年份、状态、地区和作品类型组合筛选，适合快速发现同类作品。",
                "动漫作品往往具有多个题材属性，仅靠单一分类容易遗漏内容。平台采用“主分类 + 多标签”的结构：主分类用于动作、奇幻、科幻、悬疑等大方向，标签用于战斗、校园、漫画改、异世界、推理、人工智能、时间循环等细分兴趣。用户可以组合筛选，获得更准确的动漫推荐结果。",
                512);
    }

    private void upsertNews(String title, String coverImage, String summary, String content, Integer viewCount) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM news_info WHERE title = ?", Integer.class, title);
        if (count == null || count == 0) {
            jdbcTemplate.update(
                    "INSERT INTO news_info (title, cover_image, summary, content, author_id, status, view_count) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    title, coverImage, summary, content, 1, 1, viewCount);
            return;
        }
        jdbcTemplate.update(
                "UPDATE news_info SET cover_image = ?, summary = ?, content = ?, author_id = ?, status = ?, view_count = GREATEST(view_count, ?) WHERE title = ?",
                coverImage, summary, content, 1, 1, viewCount, title);
    }

    private static class AnimeSeed {
        private final String title;
        private final String originalTitle;
        private final String coverImage;
        private final String categoryName;
        private final String region;
        private final String type;
        private final String tagNames;
        private final Integer releaseYear;
        private final String status;
        private final Integer episodes;
        private final BigDecimal score;
        private final String description;
        private final String sourceName;
        private final String watchUrl;
        private final String trailerUrl;
        private final Integer viewCount;

        private AnimeSeed(String title, String originalTitle, String coverImage, String categoryName, String region, String type,
                          String tagNames, Integer releaseYear, String status, Integer episodes, String score, String description,
                          String sourceName, String watchUrl, String trailerUrl, Integer viewCount) {
            this.title = title;
            this.originalTitle = originalTitle;
            this.coverImage = coverImage;
            this.categoryName = categoryName;
            this.region = region;
            this.type = type;
            this.tagNames = tagNames;
            this.releaseYear = releaseYear;
            this.status = status;
            this.episodes = episodes;
            this.score = new BigDecimal(score);
            this.description = description;
            this.sourceName = sourceName;
            this.watchUrl = watchUrl;
            this.trailerUrl = trailerUrl;
            this.viewCount = viewCount;
        }
    }
}
