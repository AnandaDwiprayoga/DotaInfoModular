package com.pasukanlangit.id.hero_datasource_test.cache

import com.pasukanlangit.id.hero_datasource.cache.HeroCache
import com.pasukanlangit.id.hero_domain.Hero
import com.pasukanlangit.id.hero_domain.HeroRole

class HeroCacheFake(
    private val db: HeroDatabaseFake
): HeroCache {
    override suspend fun getHero(id: Int): Hero? = db.heroes.singleOrNull { it.id == id }

    override suspend fun removeHero(id: Int) {
        db.heroes.removeIf { it.id == id}
    }

    override suspend fun selectAll(): List<Hero> =
        db.heroes

    override suspend fun insert(hero: Hero) {
        db.heroes.removeIf{ it.id == hero.id }
        db.heroes.add(hero)
    }

    override suspend fun insert(heroes: List<Hero>){
        heroes.forEach { hero ->
            db.heroes.removeIf { it.id == hero.id }
        }
        db.heroes.addAll(heroes)
    }

    override suspend fun searchByName(localizedName: String): List<Hero> =
        db.heroes.filter { it.localizedName.contains(localizedName) }

    override suspend fun searchByAttr(primaryAttr: String): List<Hero> =
        db.heroes.filter { it.primaryAttribute.uiValue == primaryAttr }

    override suspend fun searchByAttackType(attackType: String): List<Hero> =
        db.heroes.filter { it.attackType.uiValue == attackType }

    override suspend fun searchByRole(
        carry: Boolean,
        escape: Boolean,
        nuker: Boolean,
        initiator: Boolean,
        durable: Boolean,
        disabler: Boolean,
        jungler: Boolean,
        support: Boolean,
        pusher: Boolean
    ): List<Hero> {
        val heroes: MutableList<Hero> = mutableListOf()
        if(carry){
            heroes.addAll(db.heroes.filter { it.roles.contains(HeroRole.Carry) })
        }
        if(escape){
            heroes.addAll(db.heroes.filter { it.roles.contains(HeroRole.Escape) })
        }
        if(nuker){
            heroes.addAll(db.heroes.filter { it.roles.contains(HeroRole.Nuker) })
        }
        if(initiator){
            heroes.addAll(db.heroes.filter { it.roles.contains(HeroRole.Initiator) })
        }
        if(durable){
            heroes.addAll(db.heroes.filter { it.roles.contains(HeroRole.Durable) })
        }
        if(disabler){
            heroes.addAll(db.heroes.filter { it.roles.contains(HeroRole.Disabler) })
        }
        if(jungler){
            heroes.addAll(db.heroes.filter { it.roles.contains(HeroRole.Jungler) })
        }
        if(support){
            heroes.addAll(db.heroes.filter { it.roles.contains(HeroRole.Support) })
        }
        if(pusher){
            heroes.addAll(db.heroes.filter { it.roles.contains(HeroRole.Pusher) })
        }
        return heroes.distinctBy { it.id }
    }
}



