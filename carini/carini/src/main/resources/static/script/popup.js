		
		//문의함 팝업 스크립트 입니다
		var temp = 0;
		
		function verifyPassword() {
			window.open('/mypage/password', '_blank', 'width=400,height=300');
		}
		
		function pop() {
			if (temp == 0) {
				temp = 1;
				var popup = document.getElementById('popup');
				var overlay = document.getElementById('popup-overlay');
				popup.classList.add('popup');
				popup.classList.remove('popup_hidden');
				overlay.classList.add('active');
			}
		}
		
		//  popup에서 문의하기 버튼을 눌렀을 때 insertpopup으로 이동
		function to_insert_popup() {
			if (temp == 1) {
				var popup = document.getElementById('popup');
				var insertpop = document.getElementById('insertpopup');
				var overlay = document.getElementById('popup-overlay');
				popup.classList.add('popup_hidden');
				popup.classList.remove('popup');
				insertpop.classList.add('popup');
				insertpop.classList.remove('popup_hidden');
			}
		}
		
		// popup에서 글 제목을 선택했을 때 getpopup으로 이동
		function to_get_popup() {
			if (temp == 1) {
				var popup = document.getElementById('popup');
				var getpopup = document.getElementById('getpopup');
				var overlay = document.getElementById('popup-overlay');
				popup.classList.add('popup_hidden');
				popup.classList.remove('popup');
				getpopup.classList.add('popup');
				getpopup.classList.remove('popup_hidden');
			}
		}
		
		//수정기능 더미(주석처리는 하지 마세요)
		function to_edit_popup() {
			if (temp == 1) {
				var getpopup = document.getElementById('getpopup');
				var editpopup = document.getElementById('editpopup');
				getpopup.classList.add('popup_hidden');
				getpopup.classList.remove('popup');
				editpopup.classList.add('popup');
				editpopup.classList.remove('popup_hidden');
			}
		}
		
		// 문의함으로 이동
		function to_popup() {
			if (temp == 1)
				var getpopup = document.getElementById('getpopup')
			var popup = document.getElementById('popup')
			getpopup.classList.add('popup_hidden');
			getpopup.classList.remove('popup');
			popup.classList.add('popup');
			popup.classList.remove('popup_hidden');
		
		}
		
		//팝업창 닫기 기능
		function closePopup() {
			temp = 0;
			var popup = document.getElementById('popup');
			var insertpop = document.getElementById('insertpopup');
			var getpopup = document.getElementById('getpopup');
			var editpopup = document.getElementById('editpopup');
			var overlay = document.getElementById('popup-overlay');
			popup.classList.add('popup_hidden');
			popup.classList.remove('popup');
			insertpop.classList.add('popup_hidden');
			insertpop.classList.remove('popup');
			getpopup.classList.add('popup_hidden');
			getpopup.classList.remove('popup');
			editpopup.classList.add('popup_hidden');
			editpopup.classList.remove('popup');
			overlay.classList.remove('active');
		}
		
		// qna 처리결과 색상효과
		const condition = true; // 여부에 따라 
		
		document.addEventListener('DOMContentLoaded', (event) => {
			const qnaResults = document.querySelectorAll('.qna_result');
		
			if (condition) {
				qnaResults.forEach(qnaResult => {
					qnaResult.style.border = '3px solid green';
					qnaResult.style.color = 'green'; // 글씨 색상 변경
					if (qnaResult.textContent === '답변대기') {
						qnaResult.textContent = '답변완료';
					}
				});
			}
		});
		
		function submitEditedQuestion() {
			// 수정된 제목과 내용을 가져옵니다.
			var editedTitle = document.getElementById('edit_qna_title').value;
			var editedContent = document.getElementById('edit_qna').value;
		
			// 여기에 질문을 업데이트하는 로직을 추가합니다. 예를 들어, UI에서 제목과 내용을 업데이트하는 코드입니다.
			var titleElement = document.getElementById('title_area');
			var contentElement = document.getElementById('getpopup_content');
		
			titleElement.textContent = editedTitle; // 제목을 UI에 업데이트합니다.
			contentElement.textContent = editedContent; // 내용을 UI에 업데이트합니다.
		
			// 필요한 경우 추가적인 로직을 추가합니다. (예: 서버로 요청을 보내거나 다른 작업)
		
			// 수정 후 팝업을 닫습니다.
			closePopup();
		}
		
		// 문의글 작성후 저장하는 버튼입니다.
		function insert_submit() {
			closePopup();
		}

