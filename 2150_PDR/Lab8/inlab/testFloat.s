	.file	"testFloat.cpp"
	.intel_syntax noprefix
	.text
	.globl	_Z3food
	.type	_Z3food, @function
_Z3food:
.LFB0:
	.cfi_startproc
	push	ebp
	.cfi_def_cfa_offset 8
	.cfi_offset 5, -8
	mov	ebp, esp
	.cfi_def_cfa_register 5
	sub	esp, 16
	mov	eax, DWORD PTR [ebp+8]
	mov	DWORD PTR [ebp-8], eax
	mov	eax, DWORD PTR [ebp+12]
	mov	DWORD PTR [ebp-4], eax
	fld	QWORD PTR [ebp-8]
	fld	QWORD PTR .LC0
	faddp	st(1), st
	fnstcw	WORD PTR [ebp-10]
	movzx	eax, WORD PTR [ebp-10]
	mov	ah, 12
	mov	WORD PTR [ebp-12], ax
	fldcw	WORD PTR [ebp-12]
	fistp	DWORD PTR [ebp-16]
	fldcw	WORD PTR [ebp-10]
	mov	eax, DWORD PTR [ebp-16]
	leave
	.cfi_restore 5
	.cfi_def_cfa 4, 4
	ret
	.cfi_endproc
.LFE0:
	.size	_Z3food, .-_Z3food
	.globl	main
	.type	main, @function
main:
.LFB1:
	.cfi_startproc
	push	ebp
	.cfi_def_cfa_offset 8
	.cfi_offset 5, -8
	mov	ebp, esp
	.cfi_def_cfa_register 5
	and	esp, -8
	sub	esp, 8
	fld	QWORD PTR .LC2
	fstp	QWORD PTR [esp]
	call	_Z3food
	mov	eax, 0
	leave
	.cfi_restore 5
	.cfi_def_cfa 4, 4
	ret
	.cfi_endproc
.LFE1:
	.size	main, .-main
	.section	.rodata
	.align 8
.LC0:
	.long	2576980378
	.long	1075157401
	.align 8
.LC2:
	.long	3607772529
	.long	1074491555
	.ident	"GCC: (Ubuntu 4.8.2-19ubuntu1) 4.8.2"
	.section	.note.GNU-stack,"",@progbits
