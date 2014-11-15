_ZN16DevourerOfWorldsC2Ev:
	push	ebp
	mov	ebp, esp
	sub	esp, 8
	sub	esp, 12
	push	8
	call	_Znwj
	add	esp, 16
	mov	DWORD PTR [eax], 0
	mov	DWORD PTR [eax+4], 0
	mov	edx, DWORD PTR [ebp+8]
	mov	DWORD PTR [edx+4], eax
	leave
	ret
_ZN16DevourerOfWorldsD2Ev:
	push	ebp
	mov	ebp, esp
	sub	esp, 8
	mov	eax, DWORD PTR [ebp+8]
	mov	eax, DWORD PTR [eax+4]
	sub	esp, 12
	push	eax
	call	_ZdlPv
	add	esp, 16
	leave
	ret
_Z5scopev:
	push	ebp
	mov	ebp, esp
	sub	esp, 24
	sub	esp, 12
	lea	eax, [ebp-16]
	push	eax
	call	_ZN16DevourerOfWorldsC2Ev
	add	esp, 16
	sub	esp, 12
	lea	eax, [ebp-16]
	push	eax
	call	_ZN16DevourerOfWorldsD2Ev
	add	esp, 16
	leave
	ret
main:
	lea	ecx, [esp+4]
	and	esp, -16
	push	DWORD PTR [ecx-4]
	push	ebp
	mov	ebp, esp
	push	ecx
	sub	esp, 4
	call	_Z5scopev
	mov	eax, 0
	add	esp, 4
	pop	ecx
	pop	ebp
	lea	esp, [ecx-4]
	ret
_Z41__static_initialization_and_destruction_0ii:
	push	ebp
	mov	ebp, esp
	sub	esp, 8
	cmp	DWORD PTR [ebp+8], 1
	cmp	DWORD PTR [ebp+12], 65535
	sub	esp, 12
	push	OFFSET FLAT:_ZStL8__ioinit
	call	_ZNSt8ios_base4InitC1Ev
	add	esp, 16
	sub	esp, 4
	push	OFFSET FLAT:__dso_handle
	push	OFFSET FLAT:_ZStL8__ioinit
	push	OFFSET FLAT:_ZNSt8ios_base4InitD1Ev
	call	__cxa_atexit
	add	esp, 16
	leave
	ret
_GLOBAL__sub_I__ZN16DevourerOfWorldsC2Ev:
	push	ebp
	mov	ebp, esp
	sub	esp, 8
	sub	esp, 8
	push	65535
	push	1
	call	_Z41__static_initialization_and_destruction_0ii
	add	esp, 16
	leave
	ret
