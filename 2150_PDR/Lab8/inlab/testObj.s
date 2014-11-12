	.file	"testObj.cpp"
	.intel_syntax noprefix
	.globl	test
	.bss
	.align 4
	.type	test, @object
	.size	test, 8
test:
	.zero	8
	.text
	.globl	_Z3foo6simple
	.type	_Z3foo6simple, @function
_Z3foo6simple:
.LFB0:
	.cfi_startproc
	push	ebp
	.cfi_def_cfa_offset 8
	.cfi_offset 5, -8
	mov	ebp, esp
	.cfi_def_cfa_register 5
	mov	eax, DWORD PTR [ebp+8]
	pop	ebp
	.cfi_restore 5
	.cfi_def_cfa 4, 4
	ret
	.cfi_endproc
.LFE0:
	.size	_Z3foo6simple, .-_Z3foo6simple
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
	mov	DWORD PTR test, 3
	mov	DWORD PTR test+4, 4
	push	DWORD PTR test+4
	push	DWORD PTR test
	call	_Z3foo6simple
	add	esp, 8
	mov	eax, 0
	leave
	.cfi_restore 5
	.cfi_def_cfa 4, 4
	ret
	.cfi_endproc
.LFE1:
	.size	main, .-main
	.ident	"GCC: (GNU) 4.9.1 20140903 (prerelease)"
	.section	.note.GNU-stack,"",@progbits
