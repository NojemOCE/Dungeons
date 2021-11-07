# Assumptions
## Game play/settings
* Can’t change game mode during the game
* Each puzzle can be played at each of the gamemodes (peaceful, standard, hard)
* Each puzzle has initial entity placement and goals set a corresponding JSON files
* Games are only saved if the “save game” command is run, so if the game is terminated before saving then it isn’t saved
* A tick is defined as any movement or action, which includes
    * Moving positions 
    * Consuming an item (potions)
    * Building a buildable item
* You can only save the current game you are playing
* Width/height isn’t given, so a default spawn radius of x=5, y=5 is set. This is increased as entities are added to the board if their x/y coord is larger

## GameMode
* Peaceful
    * Enemies don’t attack player = mercenary doesn’t move
* Standard
    * Standard HP = 30
* Hard
    * Hard HP = 15

## Moving Entity
* Spider
    * Restriction on the spawn behaviour of spider - can’t spawn on edges of board
    * Can walk through walls and doors 
    * Define the maximum number of spiders as: 6
    * If the spider needs to reverse their direction on a tick (because they are obstructed by a boulder), then they will remain in the same position for that tick, and only start travelling in the new direction on the next tick.
    * Spider does not run away from player when it is invincible
    * HP = 3
    * Attack = 1
    * Spawn rate (or spawn strategy) = 20
* Zombie Toast
    * Zombie Toast will spawn on a cardinally adjacent cell, provided that there are no entities that zombies cannot step on
    * If there are no valid positions, no zombie will spawn until the next spawn rate
    * HP = 6
    * Attack = 2
    * Armour drop rate = 20%
* Mercenary
    * Amount of gold (treasure) required to bribe: 2
    * HP = 9
    * Attack = 3
    * Spawn rate (or spawn strategy) = 25
    * Battle radius =  5
    * Armour drop rate = 40%
    * Assume that “within 2 cardinal tiles" as a 2 tile moving distance
* Player
    * There can only be one player
    * HP - see gamemode above
    * Attack = 3
    * A player is able to attack without a weapon with the above attack stat
    * However, they cannot destroy a zombie toast spawner (as they require a weapon)
* For all:
    * Only two moving entities can be on one cell at any time
    * This implies that the character can only battle one enemy at the time
    * Once a battle starts, it cannot end until one of the characters is defeated. To continue going through ticks, the player should just just click any direction to continue onto the next attack in battle
* Given:
    * Character Health = Character Health - ((Enemy Health * Enemy Attack Damage) / 10)
    * Enemy Health = Enemy Health - ((Character Health * Character Attack Damage) / 5)
    * It is assumed that health refers to the CURRENT health of the character/enemy (rather than their initial health), i.e. they get weaker as they are defeated.

## Static Entities
* Wall
    * The walls on the edge of the map cannot be destroyed (by the bomb)
* Exit
    * Only triggers the exit win condition when player interacts with it
    * Player only wins the puzzle if other conditions are met, otherwise nothing happens
* Boulder
    * Can only be pushed by the player
    * Cannot be pushed into the static entities: wall, door, spawner. CAN be pushed onto switch, exit, portal (which teleports it)
    * Cannot be pushed onto enemies
    * Cannot be pushed onto collectable entities
* Floor Switch
    * Is triggered if a boulder is on it, and untriggered if the boulder is pushed off
    * Only triggered by boulder, otherwise acts as a standard ground cell, i.e. characters can just walk over it
* Door
    * Has a matching “keyColour” to a key which unique
    * Acts like a wall to enemies even when open (referring to reference implementation).
* Portal
    * Portals exist as pairs (A to B and B back to A)
    * This means that each map must have 2 (no less or no more) of each portal colour
* Zombie Toast Spawner
    * Can only be interacted with when the ‘interact’ command in the controller is called, otherwise is used like a wall
    * It breaks on one interaction/attack
    * Spawn rate is determined by current gamemode

## Collectable Entities
* Treasure
    * Is equivalent to “gold” required to bribe the mercenary
* Key
    * Has a matching “keyColour” to a door which unique
* Health potion
    * Restores HP by 10
* Invincibility potion
    * Spiders don’t run away from players with invincibility potion, as their movement pattern is set
    * Lasts for 10 ticks
* Invisibility potion
    * Lasts for 10 ticks
    * Player cannot be invincible at the same time as being invisible
* Bomb
    * We cannot pick these up again
    * Blast radius =  1
    * Can only be detonated if the boulder triggers the switch AFTER it has been placed (as per spec wording)
* Sword
    * Durability: 10
    * Attack power: 3
* Armour 
    * Durability: 7
* Sceptre
    * Doesn't have a durability
* For all
    * Once the character has collected an item, the item is removed from the list of game entities and added to the characters inventory
    * Health potions “can only be consumed once” - we understand this as meaning that once a health potion has been consumed, it is “discarded”,  the character is able to pick up additional health potions later on and consume them

## Rare Collectable Entities
* The One Ring: has a 10% spawn rate

## Other
* Inventory has unlimited space (no item limit)
* Weapon and shield stats are stacked
* A sword is a weapon
