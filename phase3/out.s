.data
.align 2
toAlign: .space 404
EXCEP: .asciiz "run time exception ..."
main__x: .word 0
main__y: .word 0
main__condition: .word 0

.text
.globl main
print_string: 
li $v0, 4
syscall
jr $ra
print_int: 
li $v0, 1
syscall
jr $ra
print_float: 
li $v0, 2
syscall
jr $ra
read_int: 
li $v0, 5
syscall
jr $ra
read_string: 
li $v0, 8
syscall
jr $ra
exception: 
la $a0, EXPCEP
li $v0, 4
syscall
jr $ra
b termination
main:
la $t0, main__x
li $t1, 1
sw $t1, ($t0)
la $t2, main__y
li $t3, 2
sw $t3, ($t2)
LOOP1: 
lw $t4, main__x
li $t5, 12
slt $t5,  $t4,  $t5
beqz $t5, END_LOOP8
b BEGIN_STATEMENT3
BEGIN_UPDATE2:
la $t6, main__x
lw $t7, main__x
li $t8, 1
add $t8,  $t7,  $t8
sw $t8, ($t6)
b LOOP1
BEGIN_STATEMENT3:
li.s $f1, 4.5
li.s $f2, 5.5
ori $t1,  $t1,  1
c.lt.s $f1,  $f2
bc1t myLineLabel0 
andi $t1,  $t1,  0
myLineLabel0: beqz $t1, DJZ__IF5
la $t0, main__condition
li $t3, 0
sw $t3, ($t0)
DJZ__IF5: 
lw $t2, main__x
lw $t4, main__y
sne $t4,  $t2,  $t4
beqz $t4, DJZ__IF6
la $t5, main__x
li $t7, 2
sw $t7, ($t5)
b DJP__ELSE7
DJZ__IF6:
la $t8, main__y
li $t6, 1
sw $t6, ($t8)
DJP__ELSE7:
li $t1, 1
b BEGIN_UPDATE2
END_LOOP8: 
termination: 
li $v0, 10
li $t0, 0
move $a0, $t0
syscall
