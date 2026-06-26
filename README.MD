# TeamRadar

A Fabric mod for Minecraft that shows a real-time HUD with your teammates' health, distance, direction, and dimension. Get instant alerts when someone is low on health or dies.

![Minecraft](https://img.shields.io/badge/Minecraft-1.21.4-green)
![Fabric](https://img.shields.io/badge/Fabric%20Loader-0.16.9-blue)
![Fabric API](https://img.shields.io/badge/Fabric%20API-0.119.0-blue)
![Version](https://img.shields.io/badge/Mod%20Version-1.0.0-orange)
![Environment](https://img.shields.io/badge/Environment-Client%20Side-purple)

---

## Features

### Team HUD Panel
A clean panel in the top-right corner of your screen shows every teammate with:
- Name and online/offline status
- Health bar with color coding (green/orange/red)
- Exact HP numbers
- Distance in blocks
- Direction arrow showing which way to walk
- Current dimension (OW / NETHER / END)

### Low HP Warning
Sends a chat alert the moment a teammate drops to 3 hearts or below. Alert clears automatically once they heal back up.

### Death Alert
Instant chat notification when a teammate dies, shown once per death.

### Team Management Screen
Press **T** to open a screen where you can add or remove teammates by username. Your team list is saved and persists between sessions.

---

## Keybinds

| Key | Action |
|-----|--------|
| T | Open team management screen |
| Y | Toggle HUD on/off |

Keybinds can be changed in Options > Controls > TeamRadar.

---

## How to Use

1. Launch Minecraft with the mod installed
2. Press **T** to open the team manager
3. Type each teammate's exact Minecraft username and click Add Member
4. Their info will appear in the HUD whenever they are in render distance

---

## Installation

1. Install [Fabric Loader 0.16.9+](https://fabricmc.net/use/installer)
2. Download [Fabric API 0.119.0+1.21.4](https://modrinth.com/mod/fabric-api/versions?g=1.21.4)
3. Download `teamradar-1.0.0.jar` from [Releases](../../releases)
4. Place both JARs in your `.minecraft/mods/` folder
5. Launch Minecraft with the Fabric 1.21.4 profile

---

## Building from Source

Requires Java 21.

```bash
git clone https://github.com/rahadbhuiya/TeamRadar.git
cd teamradar
gradlew build
```

Output JAR will be in `build/libs/`.

---

## Compatibility

- Works on any server including vanilla, SMP, Lifesteal, Faction, and Clan PvP servers
- Fully client-side, no server mod required
- Health and position data is only available when the teammate is within your render distance
- Teammates in a different dimension or far away will show as offline until you are in the same area
- Team list is saved per client in `config/teamradar_members.json`

---

## License

MIT