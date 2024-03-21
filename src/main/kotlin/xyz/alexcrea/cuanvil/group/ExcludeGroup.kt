package xyz.alexcrea.cuanvil.group

import org.bukkit.Material
import java.util.*

class ExcludeGroup(name: String): AbstractMaterialGroup(name) {
    override fun createDefaultSet(): EnumSet<Material> {
        return EnumSet.allOf(Material::class.java)
    }

    private var includedGroup: MutableSet<AbstractMaterialGroup> = HashSet()
    private val groupItems by lazy {createDefaultSet()}

    override fun isReferencing(other: AbstractMaterialGroup): Boolean {
        for (materialGroup in includedGroup.iterator()) {
            if((materialGroup == other) || (materialGroup.isReferencing(other))){
                return true
            }
        }
        return false
    }

    override fun addToPolicy(mat: Material) {
        includedMaterial.remove(mat)
        groupItems.remove(mat)
    }

    override fun addToPolicy(other: AbstractMaterialGroup) {
        includedGroup.add(other)
        groupItems.removeAll(other.getMaterials())
    }

    override fun setGroups(groups: MutableSet<AbstractMaterialGroup>) {
        groupItems.clear()
        groupItems.addAll(includedMaterial)

        includedGroup.clear()
        groups.forEach { group ->
            if(!group.isReferencing(this)) {
                includedGroup.add(group)
                groupItems.removeAll(group.getMaterials())
            }
        }
    }

    override fun getGroups(): MutableSet<AbstractMaterialGroup> {
        return includedGroup
    }

    override fun getMaterials(): EnumSet<Material> {
        return groupItems
    }


}