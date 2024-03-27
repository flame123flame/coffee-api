package framework.constant;

public class ResponseConstant {

	public enum RESPONSE_STATUS {
		SUCCESS, FAILED, DUPLICATE_DATA
	}
	
	public class RESPONSE_MESSAGE {
		public static final String ERROR500_CODE = "MSG_SYSTEM";
		public static final String ERROR500 = "กรุณาติดต่อผู้ดูแลระบบ";
		public static final String SUCCESS = "SUCCESS";

		public class GET {
			public static final String SUCCESS = "แสดงข้อมูลสำเร็จ";
			public static final String FAILED = "แสดงข้อมูลไม่สำเร็จ";
		}
		
		public class SAVE {
			public static final String SUCCESS = "บันทึกเรียบร้อยแล้ว";
			public static final String FAILED = "บันทึกไม่สำเร็จ";
			public static final String DUPLICATE_DATA = "มีอยู่ในระบบแล้ว";
		}
		
		public class EDIT {
			public static final String SUCCESS = "แก้ไขเรียบร้อยแล้ว";
			public static final String FAILED = "แก้ไขไม่สำเร็จ";
		}

		public class DELETE {
			public static final String SUCCESS = "ลบเรียบร้อยแล้ว";
			public static final String FAILED = "ลบไม่สำเร็จ";
		}
	}

	public static final String SHORT_DATE_FORMAT = "dd/MM/yyyy";
	public static final String SHORT_DATETIME_FORMAT = "dd/MM/yyyy HH:mm";

}
