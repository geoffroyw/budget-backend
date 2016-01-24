require 'rails_helper'

RSpec.describe Transaction, type: :model do
  it { should belong_to :bank_account }
  it { should validate_presence_of :bank_account }
  it { should validate_presence_of :name }
  it { should validate_presence_of :date }
  it { should validate_presence_of :transaction_type }
  it { should validate_presence_of :amount_cents }
  it { should validate_presence_of :amount_currency }
  it { should monetize(:amount) }
end
