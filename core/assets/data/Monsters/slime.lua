function create(monster)
  monster:setMaxHp(20)
  monster:setMaxMp(0)
  monster:createAttack("Attack1.txt",monster)
  monster:setRestingState("Monsters/Slime.png")
  monster:setDefence(10)
  monster:setSpeed(10)
  monster:setsize(96,96)
end